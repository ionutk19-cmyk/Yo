package com.whatsappautoresponder.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.whatsappautoresponder.database.AppDatabase
import com.whatsappautoresponder.database.AutoReplyRule
import kotlinx.coroutines.launch

class AutoReplyRulesViewModel(application: Application) : AndroidViewModel(application) {
    
    private val database = AppDatabase.getInstance(application)
    private val ruleDao = database.autoReplyRuleDao()
    
    val allRules: LiveData<List<AutoReplyRule>> = ruleDao.getAllRules()
    
    fun insertRule(rule: AutoReplyRule) {
        viewModelScope.launch {
            ruleDao.insertRule(rule)
        }
    }
    
    fun deleteRule(rule: AutoReplyRule) {
        viewModelScope.launch {
            ruleDao.deleteRule(rule)
        }
    }
    
    fun updateRule(rule: AutoReplyRule) {
        viewModelScope.launch {
            ruleDao.updateRule(rule)
        }
    }
}