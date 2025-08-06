package com.whatsappautoresponder.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager(context: Context) {
    
    companion object {
        private const val PREF_NAME = "whatsapp_auto_responder_prefs"
        private const val KEY_AUTO_REPLY_ENABLED = "auto_reply_enabled"
        private const val KEY_REPLY_DELAY = "reply_delay"
        private const val KEY_ONLY_WHEN_AWAY = "only_when_away"
        private const val KEY_SERVICE_ENABLED = "service_enabled"
    }
    
    private val sharedPreferences: SharedPreferences = 
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    
    fun isAutoReplyEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_AUTO_REPLY_ENABLED, false)
    }
    
    fun setAutoReplyEnabled(enabled: Boolean) {
        sharedPreferences.edit()
            .putBoolean(KEY_AUTO_REPLY_ENABLED, enabled)
            .apply()
    }
    
    fun getReplyDelay(): Int {
        return sharedPreferences.getInt(KEY_REPLY_DELAY, 5) // Default 5 seconds
    }
    
    fun setReplyDelay(delay: Int) {
        sharedPreferences.edit()
            .putInt(KEY_REPLY_DELAY, delay)
            .apply()
    }
    
    fun isOnlyWhenAway(): Boolean {
        return sharedPreferences.getBoolean(KEY_ONLY_WHEN_AWAY, false)
    }
    
    fun setOnlyWhenAway(onlyWhenAway: Boolean) {
        sharedPreferences.edit()
            .putBoolean(KEY_ONLY_WHEN_AWAY, onlyWhenAway)
            .apply()
    }
    
    fun isServiceEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_SERVICE_ENABLED, false)
    }
    
    fun setServiceEnabled(enabled: Boolean) {
        sharedPreferences.edit()
            .putBoolean(KEY_SERVICE_ENABLED, enabled)
            .apply()
    }
}