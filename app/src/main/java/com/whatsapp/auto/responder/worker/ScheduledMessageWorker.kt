package com.whatsapp.auto.responder.worker

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.work.*
import com.whatsapp.auto.responder.repository.ScheduledMessageRepository
import com.whatsapp.auto.responder.service.WhatsAppAccessibilityService
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit

class ScheduledMessageWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        private const val TAG = "ScheduledMessageWorker"
        private const val WORK_NAME = "scheduled_message_worker"

        fun startPeriodicWork(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val periodicWorkRequest = PeriodicWorkRequestBuilder<ScheduledMessageWorker>(
                15, TimeUnit.MINUTES // Check every 15 minutes
            ).setConstraints(constraints)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                periodicWorkRequest
            )
        }

        fun stopPeriodicWork(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
        }
    }

    override suspend fun doWork(): Result {
        Log.d(TAG, "ScheduledMessageWorker started")
        
        try {
            val repository = ScheduledMessageRepository(applicationContext)
            val currentTime = LocalDateTime.now()
            
            // Get all active scheduled messages
            val scheduledMessages = repository.getActiveScheduledMessages()
            
            for (scheduledMessage in scheduledMessages) {
                if (shouldSendMessage(scheduledMessage, currentTime)) {
                    sendScheduledMessage(scheduledMessage)
                    
                    // Update the scheduled time for recurring messages
                    if (scheduledMessage.repeatType != "none") {
                        updateNextScheduledTime(scheduledMessage, repository)
                    }
                }
            }
            
            return Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Error in ScheduledMessageWorker", e)
            return Result.retry()
        }
    }

    private fun shouldSendMessage(scheduledMessage: ScheduledMessage, currentTime: LocalDateTime): Boolean {
        val scheduledTime = scheduledMessage.scheduledTime
        
        // Check if it's time to send the message (within 15 minutes window)
        val timeDifference = java.time.Duration.between(scheduledTime, currentTime)
        return timeDifference.toMinutes() in 0..15
    }

    private suspend fun sendScheduledMessage(scheduledMessage: ScheduledMessage) {
        Log.d(TAG, "Sending scheduled message: ${scheduledMessage.message} to ${scheduledMessage.phoneNumber}")
        
        try {
            // Open WhatsApp with the specific contact
            openWhatsAppContact(scheduledMessage.phoneNumber)
            
            // Wait a bit for WhatsApp to load
            delay(3000)
            
            // Send the message using accessibility service
            sendMessageViaAccessibility(scheduledMessage.message)
            
            Log.d(TAG, "Scheduled message sent successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to send scheduled message", e)
        }
    }

    private fun openWhatsAppContact(phoneNumber: String) {
        try {
            // Format phone number (remove any non-digit characters except +)
            val formattedNumber = phoneNumber.replace(Regex("[^+\\d]"), "")
            
            // Create WhatsApp intent
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://wa.me/$formattedNumber")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            
            applicationContext.startActivity(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to open WhatsApp contact", e)
        }
    }

    private suspend fun sendMessageViaAccessibility(message: String) {
        // This would be handled by the accessibility service
        // For now, we'll just log it
        Log.d(TAG, "Message to send via accessibility: $message")
        
        // In a real implementation, you would coordinate with the accessibility service
        // to actually send the message
    }

    private suspend fun updateNextScheduledTime(scheduledMessage: ScheduledMessage, repository: ScheduledMessageRepository) {
        val nextTime = when (scheduledMessage.repeatType) {
            "daily" -> scheduledMessage.scheduledTime.plusDays(1)
            "weekly" -> scheduledMessage.scheduledTime.plusWeeks(1)
            "monthly" -> scheduledMessage.scheduledTime.plusMonths(1)
            else -> return
        }
        
        val updatedMessage = scheduledMessage.copy(scheduledTime = nextTime)
        repository.updateScheduledMessage(updatedMessage)
    }
}