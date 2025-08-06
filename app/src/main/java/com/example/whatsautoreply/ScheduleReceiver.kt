package com.example.whatsautoreply

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ScheduleReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra("message") ?: return

        // Launch WhatsApp conversation chooser with pre-filled text
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
            `package` = "com.whatsapp"
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        // Fallback: if WhatsApp not installed, do nothing
        try {
            context.startActivity(sendIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}