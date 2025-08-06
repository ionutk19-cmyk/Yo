package com.whatsapp.auto.responder.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.whatsapp.auto.responder.worker.ScheduledMessageWorker

class BootReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "BootReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_MY_PACKAGE_REPLACED -> {
                Log.d(TAG, "Boot completed or package replaced")
                
                // Start the scheduled message worker
                ScheduledMessageWorker.startPeriodicWork(context)
                
                Log.d(TAG, "Scheduled message worker started")
            }
        }
    }
}