package com.example.calendarapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String? = null,
    val startTime: Date,
    val endTime: Date,
    val location: String? = null,
    val isAllDay: Boolean = false,
    val color: Int = 0xFF2196F3.toInt(), // Default blue color
    val remindBefore: Long = 0, // Minutes before event to remind
    val isRecurring: Boolean = false,
    val recurrencePattern: String? = null, // Daily, Weekly, Monthly, Yearly
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)