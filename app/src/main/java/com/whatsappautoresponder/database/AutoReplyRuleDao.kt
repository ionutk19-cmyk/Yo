package com.whatsappautoresponder.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AutoReplyRuleDao {
    
    @Query("SELECT * FROM auto_reply_rules ORDER BY createdAt DESC")
    fun getAllRules(): LiveData<List<AutoReplyRule>>
    
    @Query("SELECT * FROM auto_reply_rules WHERE isEnabled = 1 ORDER BY createdAt DESC")
    fun getEnabledRules(): LiveData<List<AutoReplyRule>>
    
    @Query("SELECT * FROM auto_reply_rules WHERE id = :id")
    suspend fun getRuleById(id: Long): AutoReplyRule?
    
    @Insert
    suspend fun insertRule(rule: AutoReplyRule): Long
    
    @Update
    suspend fun updateRule(rule: AutoReplyRule)
    
    @Delete
    suspend fun deleteRule(rule: AutoReplyRule)
    
    @Query("DELETE FROM auto_reply_rules WHERE id = :id")
    suspend fun deleteRuleById(id: Long)
    
    @Query("UPDATE auto_reply_rules SET isEnabled = :isEnabled WHERE id = :id")
    suspend fun toggleRuleEnabled(id: Long, isEnabled: Boolean)
}