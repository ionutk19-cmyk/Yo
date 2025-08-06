package com.whatsapp.auto.responder.viewmodel

import android.app.Application
import android.content.Context
import android.provider.Settings
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.whatsapp.auto.responder.model.ScheduledMessage
import com.whatsapp.auto.responder.repository.ScheduledMessageRepository
import com.whatsapp.auto.responder.service.WhatsAppAccessibilityService
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ScheduledMessageRepository(application)
    
    private val _accessibilityServiceEnabled = MutableLiveData<Boolean>()
    val accessibilityServiceEnabled: LiveData<Boolean> = _accessibilityServiceEnabled

    private val _autoReplyEnabled = MutableLiveData<Boolean>()
    val autoReplyEnabled: LiveData<Boolean> = _autoReplyEnabled

    private val _scheduledMessages = MutableLiveData<List<ScheduledMessage>>()
    val scheduledMessages: LiveData<List<ScheduledMessage>> = _scheduledMessages

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    init {
        loadAutoReplySettings()
        loadScheduledMessages()
    }

    fun checkAccessibilityServiceStatus() {
        val isEnabled = isAccessibilityServiceEnabled()
        _accessibilityServiceEnabled.value = isEnabled
    }

    private fun isAccessibilityServiceEnabled(): Boolean {
        val context = getApplication<Application>()
        val accessibilityEnabled = Settings.Secure.getInt(
            context.contentResolver,
            Settings.Secure.ACCESSIBILITY_ENABLED, 0
        )

        if (accessibilityEnabled == 1) {
            val service = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            return service?.contains(context.packageName + "/" + WhatsAppAccessibilityService::class.java.name) == true
        }
        return false
    }

    fun setAutoReplyEnabled(enabled: Boolean) {
        _autoReplyEnabled.value = enabled
        saveAutoReplySettings()
    }

    fun saveAutoReplySettings(message: String? = null, replyType: String? = null) {
        val context = getApplication<Application>()
        val sharedPrefs = context.getSharedPreferences("auto_reply_settings", Context.MODE_PRIVATE)
        
        with(sharedPrefs.edit()) {
            putBoolean("auto_reply_enabled", _autoReplyEnabled.value ?: false)
            message?.let { putString("auto_reply_message", it) }
            replyType?.let { putString("reply_type", it) }
            apply()
        }
    }

    private fun loadAutoReplySettings() {
        val context = getApplication<Application>()
        val sharedPrefs = context.getSharedPreferences("auto_reply_settings", Context.MODE_PRIVATE)
        
        _autoReplyEnabled.value = sharedPrefs.getBoolean("auto_reply_enabled", false)
    }

    fun getAutoReplyMessage(): String {
        val context = getApplication<Application>()
        val sharedPrefs = context.getSharedPreferences("auto_reply_settings", Context.MODE_PRIVATE)
        return sharedPrefs.getString("auto_reply_message", "") ?: ""
    }

    fun getReplyType(): String {
        val context = getApplication<Application>()
        val sharedPrefs = context.getSharedPreferences("auto_reply_settings", Context.MODE_PRIVATE)
        return sharedPrefs.getString("reply_type", "all") ?: "all"
    }

    fun addScheduledMessage(message: String, phoneNumber: String, time: LocalDateTime, repeatType: String) {
        viewModelScope.launch {
            try {
                val scheduledMessage = ScheduledMessage(
                    message = message,
                    phoneNumber = phoneNumber,
                    scheduledTime = time,
                    repeatType = repeatType
                )
                repository.insertScheduledMessage(scheduledMessage)
                _message.value = "Schedule saved successfully"
                loadScheduledMessages()
            } catch (e: Exception) {
                _message.value = "Failed to save schedule: ${e.message}"
            }
        }
    }

    fun updateScheduledMessage(scheduledMessage: ScheduledMessage) {
        viewModelScope.launch {
            try {
                repository.updateScheduledMessage(scheduledMessage)
                _message.value = "Schedule updated successfully"
                loadScheduledMessages()
            } catch (e: Exception) {
                _message.value = "Failed to update schedule: ${e.message}"
            }
        }
    }

    fun deleteScheduledMessage(scheduledMessage: ScheduledMessage) {
        viewModelScope.launch {
            try {
                repository.deleteScheduledMessage(scheduledMessage)
                _message.value = "Schedule deleted successfully"
                loadScheduledMessages()
            } catch (e: Exception) {
                _message.value = "Failed to delete schedule: ${e.message}"
            }
        }
    }

    private fun loadScheduledMessages() {
        viewModelScope.launch {
            try {
                val messages = repository.getAllScheduledMessages()
                _scheduledMessages.value = messages
            } catch (e: Exception) {
                _message.value = "Failed to load scheduled messages: ${e.message}"
            }
        }
    }
}