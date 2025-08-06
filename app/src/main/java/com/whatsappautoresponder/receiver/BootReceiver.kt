package com.whatsappautoresponder.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.whatsappautoresponder.utils.SharedPreferencesManager
import com.whatsappautoresponder.worker.ScheduledMessageWorker
import java.util.concurrent.TimeUnit

class BootReceiver : BroadcastReceiver() {
    
    companion object {
        private const val TAG = "BootReceiver"
        private const val SCHEDULED_MESSAGE_WORK_NAME = "scheduled_message_work"
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED || 
            intent.action == "android.intent.action.QUICKBOOT_POWERON") {
            
            Log.d(TAG, "Device boot completed, starting scheduled message worker")
            
            val prefsManager = SharedPreferencesManager(context)
            if (prefsManager.isServiceEnabled()) {
                startScheduledMessageWorker(context)
            }
        }
    }
    
    private fun startScheduledMessageWorker(context: Context) {
        val workRequest = PeriodicWorkRequestBuilder<ScheduledMessageWorker>(
            15, TimeUnit.MINUTES // Check for scheduled messages every 15 minutes
        ).build()
        
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            SCHEDULED_MESSAGE_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}