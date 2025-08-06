package com.whatsappautoresponder.worker

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.whatsappautoresponder.database.AppDatabase
import com.whatsappautoresponder.database.ScheduledMessage
import com.whatsappautoresponder.receiver.ScheduledMessageReceiver
import java.util.*

class ScheduledMessageWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    companion object {
        private const val TAG = "ScheduledMessageWorker"
    }

    override suspend fun doWork(): Result {
        return try {
            Log.d(TAG, "Checking for pending scheduled messages")
            
            val database = AppDatabase.getInstance(applicationContext)
            val currentTime = System.currentTimeMillis()
            val pendingMessages = database.scheduledMessageDao().getPendingMessages(currentTime)
            
            pendingMessages.forEach { message ->
                scheduleMessageAlarm(message)
            }
            
            // Handle recurring messages
            handleRecurringMessages(database)
            
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Error in ScheduledMessageWorker", e)
            Result.retry()
        }
    }

    private fun scheduleMessageAlarm(message: ScheduledMessage) {
        val alarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        
        val intent = Intent(applicationContext, ScheduledMessageReceiver::class.java).apply {
            putExtra(ScheduledMessageReceiver.EXTRA_MESSAGE_ID, message.id)
            putExtra(ScheduledMessageReceiver.EXTRA_CONTACT_NAME, message.contactName)
            putExtra(ScheduledMessageReceiver.EXTRA_MESSAGE_TEXT, message.message)
        }
        
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            message.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                message.scheduledTime,
                pendingIntent
            )
            Log.d(TAG, "Scheduled alarm for message ID: ${message.id} at ${Date(message.scheduledTime)}")
        } catch (e: SecurityException) {
            Log.e(TAG, "Permission denied to set exact alarm", e)
            // Fallback to inexact alarm
            alarmManager.set(AlarmManager.RTC_WAKEUP, message.scheduledTime, pendingIntent)
        }
    }

    private suspend fun handleRecurringMessages(database: AppDatabase) {
        val completedRecurringMessages = database.scheduledMessageDao().getCompletedRecurringMessages()
        
        completedRecurringMessages.forEach { message ->
            val nextTime = calculateNextOccurrence(message.scheduledTime, message.recurringPattern)
            if (nextTime > 0) {
                val newMessage = message.copy(
                    id = 0, // Let Room auto-generate new ID
                    scheduledTime = nextTime,
                    isCompleted = false,
                    createdAt = System.currentTimeMillis()
                )
                database.scheduledMessageDao().insertScheduledMessage(newMessage)
                Log.d(TAG, "Created new recurring message for ${Date(nextTime)}")
            }
        }
    }

    private fun calculateNextOccurrence(lastTime: Long, pattern: String): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = lastTime
        }
        
        return when (pattern.lowercase()) {
            "daily" -> {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
                calendar.timeInMillis
            }
            "weekly" -> {
                calendar.add(Calendar.WEEK_OF_YEAR, 1)
                calendar.timeInMillis
            }
            "monthly" -> {
                calendar.add(Calendar.MONTH, 1)
                calendar.timeInMillis
            }
            else -> 0
        }
    }
}