package com.whatsappautoresponder.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.whatsappautoresponder.database.AppDatabase
import com.whatsappautoresponder.service.WhatsAppAccessibilityService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScheduledMessageReceiver : BroadcastReceiver() {
    
    companion object {
        private const val TAG = "ScheduledMessageReceiver"
        const val EXTRA_MESSAGE_ID = "message_id"
        const val EXTRA_CONTACT_NAME = "contact_name"
        const val EXTRA_MESSAGE_TEXT = "message_text"
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        val messageId = intent.getLongExtra(EXTRA_MESSAGE_ID, -1L)
        val contactName = intent.getStringExtra(EXTRA_CONTACT_NAME) ?: return
        val messageText = intent.getStringExtra(EXTRA_MESSAGE_TEXT) ?: return
        
        Log.d(TAG, "Received scheduled message alarm for contact: $contactName")
        
        // Send the message through accessibility service
        val accessibilityService = WhatsAppAccessibilityService.instance
        if (accessibilityService != null) {
            accessibilityService.sendScheduledMessage(contactName, messageText)
            
            // Mark message as completed in database
            if (messageId != -1L) {
                val database = AppDatabase.getInstance(context)
                CoroutineScope(Dispatchers.IO).launch {
                    database.scheduledMessageDao().markMessageCompleted(messageId)
                }
            }
        } else {
            Log.w(TAG, "Accessibility service not available, cannot send scheduled message")
        }
    }
}