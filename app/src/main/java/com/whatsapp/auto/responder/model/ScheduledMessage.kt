package com.whatsapp.auto.responder.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "scheduled_messages")
data class ScheduledMessage(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val message: String,
    val phoneNumber: String,
    val scheduledTime: LocalDateTime,
    val repeatType: String, // "none", "daily", "weekly", "monthly"
    val isActive: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now()
)