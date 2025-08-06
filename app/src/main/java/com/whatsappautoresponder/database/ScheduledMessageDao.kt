package com.whatsappautoresponder.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ScheduledMessageDao {
    
    @Query("SELECT * FROM scheduled_messages ORDER BY scheduledTime ASC")
    fun getAllScheduledMessages(): LiveData<List<ScheduledMessage>>
    
    @Query("SELECT * FROM scheduled_messages WHERE isCompleted = 0 AND scheduledTime <= :currentTime ORDER BY scheduledTime ASC")
    suspend fun getPendingMessages(currentTime: Long): List<ScheduledMessage>
    
    @Query("SELECT * FROM scheduled_messages WHERE id = :id")
    suspend fun getScheduledMessageById(id: Long): ScheduledMessage?
    
    @Insert
    suspend fun insertScheduledMessage(message: ScheduledMessage): Long
    
    @Update
    suspend fun updateScheduledMessage(message: ScheduledMessage)
    
    @Delete
    suspend fun deleteScheduledMessage(message: ScheduledMessage)
    
    @Query("DELETE FROM scheduled_messages WHERE id = :id")
    suspend fun deleteScheduledMessageById(id: Long)
    
    @Query("UPDATE scheduled_messages SET isCompleted = 1 WHERE id = :id")
    suspend fun markMessageCompleted(id: Long)
    
    @Query("SELECT * FROM scheduled_messages WHERE isRecurring = 1 AND isCompleted = 1")
    suspend fun getCompletedRecurringMessages(): List<ScheduledMessage>
}