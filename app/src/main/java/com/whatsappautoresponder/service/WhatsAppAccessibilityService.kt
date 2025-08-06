package com.whatsappautoresponder.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.lifecycle.Observer
import com.whatsappautoresponder.database.AppDatabase
import com.whatsappautoresponder.database.AutoReplyRule
import com.whatsappautoresponder.utils.SharedPreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WhatsAppAccessibilityService : AccessibilityService() {

    companion object {
        private const val TAG = "WhatsAppAccessibility"
        private const val WHATSAPP_PACKAGE = "com.whatsapp"
        var instance: WhatsAppAccessibilityService? = null
    }

    private lateinit var database: AppDatabase
    private lateinit var prefsManager: SharedPreferencesManager
    private val handler = Handler(Looper.getMainLooper())
    private val serviceScope = CoroutineScope(Dispatchers.IO)
    private var autoReplyRules: List<AutoReplyRule> = emptyList()

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
        database = AppDatabase.getInstance(this)
        prefsManager = SharedPreferencesManager(this)
        
        // Observe auto reply rules changes
        database.autoReplyRuleDao().getAllRules().observeForever { rules ->
            autoReplyRules = rules ?: emptyList()
        }
        
        Log.d(TAG, "WhatsApp Accessibility Service connected")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (!prefsManager.isAutoReplyEnabled()) return
        
        event?.let { accessibilityEvent ->
            when (accessibilityEvent.eventType) {
                AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED -> {
                    handleNotification(accessibilityEvent)
                }
                AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {
                    if (accessibilityEvent.packageName == WHATSAPP_PACKAGE) {
                        handleWindowContentChanged(accessibilityEvent)
                    }
                }
            }
        }
    }

    private fun handleNotification(event: AccessibilityEvent) {
        val packageName = event.packageName?.toString()
        if (packageName == WHATSAPP_PACKAGE) {
            val notification = event.text?.joinToString(" ")
            Log.d(TAG, "WhatsApp notification: $notification")
            
            notification?.let { notificationText ->
                processMessage(notificationText, null)
            }
        }
    }

    private fun handleWindowContentChanged(event: AccessibilityEvent) {
        if (event.source == null) return
        
        try {
            val rootNode = rootInActiveWindow ?: return
            val messageNodes = findMessageNodes(rootNode)
            
            messageNodes.forEach { messageNode ->
                val messageText = messageNode.text?.toString()
                val senderNode = findSenderNode(messageNode)
                val senderName = senderNode?.text?.toString()
                
                if (!messageText.isNullOrEmpty()) {
                    Log.d(TAG, "Message from $senderName: $messageText")
                    processMessage(messageText, senderName)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error processing window content", e)
        }
    }

    private fun findMessageNodes(rootNode: AccessibilityNodeInfo): List<AccessibilityNodeInfo> {
        val messageNodes = mutableListOf<AccessibilityNodeInfo>()
        
        // Look for message bubbles - these typically have specific class names or content descriptions
        fun traverse(node: AccessibilityNodeInfo) {
            // Check if this node contains a message
            if (isMessageNode(node)) {
                messageNodes.add(node)
            }
            
            for (i in 0 until node.childCount) {
                node.getChild(i)?.let { traverse(it) }
            }
        }
        
        traverse(rootNode)
        return messageNodes
    }

    private fun isMessageNode(node: AccessibilityNodeInfo): Boolean {
        val className = node.className?.toString()
        val viewId = node.viewIdResourceName
        
        // WhatsApp message bubbles typically have these characteristics
        return (className?.contains("TextView") == true && 
               (viewId?.contains("message") == true || 
                viewId?.contains("chat_message") == true ||
                node.contentDescription?.contains("message", ignoreCase = true) == true))
    }

    private fun findSenderNode(messageNode: AccessibilityNodeInfo): AccessibilityNodeInfo? {
        var parent = messageNode.parent
        while (parent != null) {
            // Look for sender name in parent or sibling nodes
            for (i in 0 until parent.childCount) {
                val child = parent.getChild(i)
                if (child != null && child != messageNode) {
                    val text = child.text?.toString()
                    if (!text.isNullOrEmpty() && text.length < 50 && !text.contains(":")) {
                        return child
                    }
                }
            }
            parent = parent.parent
        }
        return null
    }

    private fun processMessage(messageText: String, senderName: String?) {
        serviceScope.launch {
            try {
                // Find matching auto reply rule
                val matchingRule = autoReplyRules.find { rule ->
                    when {
                        rule.contactName.isNotEmpty() && senderName != null -> {
                            senderName.contains(rule.contactName, ignoreCase = true) &&
                            (rule.keyword.isEmpty() || messageText.contains(rule.keyword, ignoreCase = true))
                        }
                        rule.keyword.isNotEmpty() -> {
                            messageText.contains(rule.keyword, ignoreCase = true)
                        }
                        else -> false
                    }
                }

                matchingRule?.let { rule ->
                    Log.d(TAG, "Found matching rule: ${rule.keyword} -> ${rule.response}")
                    
                    // Apply delay if configured
                    val delay = prefsManager.getReplyDelay() * 1000L
                    if (delay > 0) {
                        Thread.sleep(delay)
                    }
                    
                    // Send auto reply
                    handler.post {
                        sendAutoReply(rule.response)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error processing message", e)
            }
        }
    }

    private fun sendAutoReply(response: String) {
        try {
            val rootNode = rootInActiveWindow ?: return
            val inputField = findInputField(rootNode)
            
            inputField?.let { field ->
                // Set text in input field
                val arguments = android.os.Bundle()
                arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, response)
                field.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments)
                
                // Find and click send button
                val sendButton = findSendButton(rootNode)
                sendButton?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                
                Log.d(TAG, "Auto reply sent: $response")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error sending auto reply", e)
        }
    }

    private fun findInputField(rootNode: AccessibilityNodeInfo): AccessibilityNodeInfo? {
        fun traverse(node: AccessibilityNodeInfo): AccessibilityNodeInfo? {
            val className = node.className?.toString()
            val viewId = node.viewIdResourceName
            
            // Look for WhatsApp message input field
            if (className == "android.widget.EditText" && 
                (viewId?.contains("entry") == true || 
                 viewId?.contains("input") == true ||
                 node.hint?.contains("message", ignoreCase = true) == true)) {
                return node
            }
            
            for (i in 0 until node.childCount) {
                node.getChild(i)?.let { child ->
                    traverse(child)?.let { return it }
                }
            }
            return null
        }
        
        return traverse(rootNode)
    }

    private fun findSendButton(rootNode: AccessibilityNodeInfo): AccessibilityNodeInfo? {
        fun traverse(node: AccessibilityNodeInfo): AccessibilityNodeInfo? {
            val className = node.className?.toString()
            val viewId = node.viewIdResourceName
            val contentDesc = node.contentDescription?.toString()
            
            // Look for WhatsApp send button
            if ((className?.contains("ImageButton") == true || className?.contains("Button") == true) &&
                (viewId?.contains("send") == true || 
                 contentDesc?.contains("send", ignoreCase = true) == true)) {
                return node
            }
            
            for (i in 0 until node.childCount) {
                node.getChild(i)?.let { child ->
                    traverse(child)?.let { return it }
                }
            }
            return null
        }
        
        return traverse(rootNode)
    }

    override fun onInterrupt() {
        Log.d(TAG, "WhatsApp Accessibility Service interrupted")
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
        Log.d(TAG, "WhatsApp Accessibility Service destroyed")
    }

    // Public method to send scheduled messages
    fun sendScheduledMessage(contactName: String, message: String) {
        serviceScope.launch {
            try {
                // Open WhatsApp chat with specific contact
                openWhatsAppChat(contactName)
                
                // Wait for chat to load
                Thread.sleep(2000)
                
                // Send message
                handler.post {
                    sendAutoReply(message)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error sending scheduled message", e)
            }
        }
    }

    private fun openWhatsAppChat(contactName: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setPackage(WHATSAPP_PACKAGE)
                putExtra("jid", "$contactName@s.whatsapp.net")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Error opening WhatsApp chat", e)
        }
    }
}