package com.whatsapp.auto.responder.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.whatsapp.auto.responder.database.AppDatabase
import com.whatsapp.auto.responder.model.ScheduledMessage

class ScheduledMessageRepository(context: Context) {
    
    private val scheduledMessageDao = AppDatabase.getInstance(context).scheduledMessageDao()

    suspend fun insertScheduledMessage(scheduledMessage: ScheduledMessage) {
        scheduledMessageDao.insertScheduledMessage(scheduledMessage)
    }

    suspend fun updateScheduledMessage(scheduledMessage: ScheduledMessage) {
        scheduledMessageDao.updateScheduledMessage(scheduledMessage)
    }

    suspend fun deleteScheduledMessage(scheduledMessage: ScheduledMessage) {
        scheduledMessageDao.deleteScheduledMessage(scheduledMessage)
    }

    suspend fun getAllScheduledMessages(): List<ScheduledMessage> {
        return scheduledMessageDao.getAllScheduledMessages()
    }

    suspend fun getActiveScheduledMessages(): List<ScheduledMessage> {
        return scheduledMessageDao.getActiveScheduledMessages()
    }

    suspend fun getScheduledMessagesByTime(time: java.time.LocalDateTime): List<ScheduledMessage> {
        return scheduledMessageDao.getScheduledMessagesByTime(time)
    }
}