package com.whatsappautoresponder.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scheduled_messages")
data class ScheduledMessage(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val contactName: String = "",
    val message: String = "",
    val scheduledTime: Long = 0,
    val isRecurring: Boolean = false,
    val recurringPattern: String = "", // "daily", "weekly", "monthly"
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)