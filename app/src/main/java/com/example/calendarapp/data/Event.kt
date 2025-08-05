package com.example.calendarapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String? = null,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
    val location: String? = null,
    val reminderMinutes: Int = 15, // Default 15 minutes before
    val color: Int = 0xFF2196F3.toInt(), // Default blue color
    val isAllDay: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    fun getDurationInMinutes(): Long {
        return java.time.Duration.between(startDateTime, endDateTime).toMinutes()
    }

    fun isToday(): Boolean {
        val today = LocalDateTime.now().toLocalDate()
        return startDateTime.toLocalDate() == today
    }

    fun isThisWeek(): Boolean {
        val today = LocalDateTime.now()
        val startOfWeek = today.toLocalDate().minusDays(today.dayOfWeek.value.toLong() - 1)
        val endOfWeek = startOfWeek.plusDays(6)
        val eventDate = startDateTime.toLocalDate()
        return eventDate >= startOfWeek && eventDate <= endOfWeek
    }

    fun isThisMonth(): Boolean {
        val today = LocalDateTime.now()
        return startDateTime.year == today.year && startDateTime.month == today.month
    }
}