package com.whatsapp.auto.responder.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import com.whatsapp.auto.responder.worker.ScheduledMessageWorker
import kotlinx.coroutines.*
import java.time.LocalDateTime

class WhatsAppAccessibilityService : AccessibilityService() {

    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var isWhatsAppOpen = false
    private var lastMessageTime = 0L
    private val messageCooldown = 2000L // 2 seconds cooldown between auto-replies

    companion object {
        private const val TAG = "WhatsAppAccessibility"
        private const val WHATSAPP_PACKAGE = "com.whatsapp"
        private const val WHATSAPP_BUSINESS_PACKAGE = "com.whatsapp.w4b"
        
        var isServiceRunning = false
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d(TAG, "Accessibility Service Connected")
        isServiceRunning = true
        
        val info = AccessibilityServiceInfo()
        info.apply {
            eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED or
                        AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED or
                        AccessibilityEvent.TYPE_VIEW_CLICKED or
                        AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED
            
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            
            flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS or
                    AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS or
                    AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY
            
            notificationTimeout = 100
        }
        
        serviceInfo = info
        
        // Start scheduled message worker
        ScheduledMessageWorker.startPeriodicWork(this)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.packageName == null) return
        
        when (event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                handleWindowStateChanged(event)
            }
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {
                handleWindowContentChanged(event)
            }
            AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED -> {
                handleNotificationStateChanged(event)
            }
        }
    }

    private fun handleWindowStateChanged(event: AccessibilityEvent) {
        when (event.packageName.toString()) {
            WHATSAPP_PACKAGE, WHATSAPP_BUSINESS_PACKAGE -> {
                isWhatsAppOpen = true
                Log.d(TAG, "WhatsApp window opened")
            }
            else -> {
                isWhatsAppOpen = false
            }
        }
    }

    private fun handleWindowContentChanged(event: AccessibilityEvent) {
        if (!isWhatsAppOpen || event.packageName.toString() !in listOf(WHATSAPP_PACKAGE, WHATSAPP_BUSINESS_PACKAGE)) {
            return
        }

        val currentTime = System.currentTimeMillis()
        if (currentTime - lastMessageTime < messageCooldown) {
            return
        }

        // Check if auto-reply is enabled
        if (!isAutoReplyEnabled()) {
            return
        }

        val rootNode = rootInActiveWindow ?: return
        
        // Look for new message indicators
        val newMessageNodes = findNewMessageNodes(rootNode)
        if (newMessageNodes.isNotEmpty()) {
            serviceScope.launch {
                handleNewMessages(newMessageNodes)
            }
        }
    }

    private fun handleNotificationStateChanged(event: AccessibilityEvent) {
        if (event.packageName.toString() in listOf(WHATSAPP_PACKAGE, WHATSAPP_BUSINESS_PACKAGE)) {
            Log.d(TAG, "WhatsApp notification detected")
            // You can add logic here to handle notifications
        }
    }

    private fun findNewMessageNodes(rootNode: AccessibilityNodeInfo): List<AccessibilityNodeInfo> {
        val newMessageNodes = mutableListOf<AccessibilityNodeInfo>()
        
        // Look for message bubbles or text nodes that might indicate new messages
        val messageNodes = rootNode.findAccessibilityNodeInfosByViewId("com.whatsapp:id/message_text")
        val bubbleNodes = rootNode.findAccessibilityNodeInfosByViewId("com.whatsapp:id/bubble")
        
        // Add logic to identify new messages (this is a simplified approach)
        messageNodes.forEach { node ->
            if (node.text != null && node.text.toString().isNotEmpty()) {
                newMessageNodes.add(node)
            }
        }
        
        return newMessageNodes
    }

    private suspend fun handleNewMessages(messageNodes: List<AccessibilityNodeInfo>) {
        val autoReplyMessage = getAutoReplyMessage()
        if (autoReplyMessage.isEmpty()) {
            return
        }

        // Find the send button and text input field
        val rootNode = rootInActiveWindow ?: return
        
        val sendButton = findSendButton(rootNode)
        val textInput = findTextInput(rootNode)
        
        if (sendButton != null && textInput != null) {
            // Type the auto-reply message
            textInput.performAction(AccessibilityNodeInfo.ACTION_FOCUS)
            textInput.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, 
                android.os.Bundle().apply {
                    putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, autoReplyMessage)
                }
            )
            
            // Send the message
            sendButton.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            
            lastMessageTime = System.currentTimeMillis()
            Log.d(TAG, "Auto-reply sent: $autoReplyMessage")
            
            withContext(Dispatchers.Main) {
                Toast.makeText(this@WhatsAppAccessibilityService, "Auto-reply sent", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun findSendButton(rootNode: AccessibilityNodeInfo): AccessibilityNodeInfo? {
        // Try different possible IDs for the send button
        val sendButtonIds = listOf(
            "com.whatsapp:id/send",
            "com.whatsapp:id/btn_send",
            "com.whatsapp:id/send_button"
        )
        
        for (id in sendButtonIds) {
            val nodes = rootNode.findAccessibilityNodeInfosByViewId(id)
            if (nodes.isNotEmpty()) {
                return nodes[0]
            }
        }
        
        // Fallback: look for button with send-related text
        return findNodeByText(rootNode, "send", "Send", "▶")
    }

    private fun findTextInput(rootNode: AccessibilityNodeInfo): AccessibilityNodeInfo? {
        // Try different possible IDs for the text input
        val textInputIds = listOf(
            "com.whatsapp:id/entry",
            "com.whatsapp:id/message_input",
            "com.whatsapp:id/edit_text"
        )
        
        for (id in textInputIds) {
            val nodes = rootNode.findAccessibilityNodeInfosByViewId(id)
            if (nodes.isNotEmpty()) {
                return nodes[0]
            }
        }
        
        // Fallback: look for EditText nodes
        return findEditTextNode(rootNode)
    }

    private fun findNodeByText(rootNode: AccessibilityNodeInfo, vararg texts: String): AccessibilityNodeInfo? {
        for (i in 0 until rootNode.childCount) {
            val child = rootNode.getChild(i) ?: continue
            
            val childText = child.text?.toString()?.lowercase()
            if (childText != null && texts.any { it.lowercase() in childText }) {
                return child
            }
            
            val result = findNodeByText(child, *texts)
            if (result != null) {
                return result
            }
        }
        return null
    }

    private fun findEditTextNode(rootNode: AccessibilityNodeInfo): AccessibilityNodeInfo? {
        for (i in 0 until rootNode.childCount) {
            val child = rootNode.getChild(i) ?: continue
            
            if (child.className == "android.widget.EditText") {
                return child
            }
            
            val result = findEditTextNode(child)
            if (result != null) {
                return result
            }
        }
        return null
    }

    private fun isAutoReplyEnabled(): Boolean {
        val sharedPrefs = getSharedPreferences("auto_reply_settings", Context.MODE_PRIVATE)
        return sharedPrefs.getBoolean("auto_reply_enabled", false)
    }

    private fun getAutoReplyMessage(): String {
        val sharedPrefs = getSharedPreferences("auto_reply_settings", Context.MODE_PRIVATE)
        return sharedPrefs.getString("auto_reply_message", "") ?: ""
    }

    override fun onInterrupt() {
        Log.d(TAG, "Accessibility Service Interrupted")
    }

    override fun onDestroy() {
        super.onDestroy()
        isServiceRunning = false
        serviceScope.cancel()
        Log.d(TAG, "Accessibility Service Destroyed")
    }
}