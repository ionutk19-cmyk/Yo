package com.whatsappautoresponder.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.whatsappautoresponder.database.AppDatabase
import com.whatsappautoresponder.database.ScheduledMessage
import kotlinx.coroutines.launch

class ScheduledMessagesViewModel(application: Application) : AndroidViewModel(application) {
    
    private val database = AppDatabase.getInstance(application)
    private val messageDao = database.scheduledMessageDao()
    
    val allMessages: LiveData<List<ScheduledMessage>> = messageDao.getAllScheduledMessages()
    
    fun insertMessage(message: ScheduledMessage) {
        viewModelScope.launch {
            messageDao.insertScheduledMessage(message)
        }
    }
    
    fun deleteMessage(message: ScheduledMessage) {
        viewModelScope.launch {
            messageDao.deleteScheduledMessage(message)
        }
    }
    
    fun updateMessage(message: ScheduledMessage) {
        viewModelScope.launch {
            messageDao.updateScheduledMessage(message)
        }
    }
}