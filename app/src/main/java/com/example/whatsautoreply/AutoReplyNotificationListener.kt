package com.example.whatsautoreply

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.os.Bundle
import android.app.RemoteInput
import android.content.Intent

class AutoReplyNotificationListener : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        // Only handle WhatsApp notifications
        if (sbn.packageName != "com.whatsapp") return

        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val replyText = prefs.getString("auto_reply", null) ?: return

        val notification = sbn.notification
        val actions = notification.actions ?: return

        for (action in actions) {
            val remoteInputs = action.remoteInputs ?: continue
            for (remoteInput in remoteInputs) {
                if (remoteInput.resultKey != null) {
                    val bundle = Bundle()
                    bundle.putCharSequence(remoteInput.resultKey, replyText)
                    val intent = Intent().addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
                    RemoteInput.addResultsToIntent(arrayOf(remoteInput), intent, bundle)
                    try {
                        action.actionIntent.send(this, 0, intent)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    break
                }
            }
        }
    }
}