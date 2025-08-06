package com.whatsapp.auto.responder.database

import androidx.room.*
import com.whatsapp.auto.responder.model.ScheduledMessage
import java.time.LocalDateTime

@Dao
interface ScheduledMessageDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScheduledMessage(scheduledMessage: ScheduledMessage)

    @Update
    suspend fun updateScheduledMessage(scheduledMessage: ScheduledMessage)

    @Delete
    suspend fun deleteScheduledMessage(scheduledMessage: ScheduledMessage)

    @Query("SELECT * FROM scheduled_messages ORDER BY scheduledTime ASC")
    suspend fun getAllScheduledMessages(): List<ScheduledMessage>

    @Query("SELECT * FROM scheduled_messages WHERE isActive = 1 ORDER BY scheduledTime ASC")
    suspend fun getActiveScheduledMessages(): List<ScheduledMessage>

    @Query("SELECT * FROM scheduled_messages WHERE scheduledTime <= :time AND isActive = 1")
    suspend fun getScheduledMessagesByTime(time: LocalDateTime): List<ScheduledMessage>

    @Query("SELECT * FROM scheduled_messages WHERE id = :id")
    suspend fun getScheduledMessageById(id: Long): ScheduledMessage?
}