package com.example.calendarapp

data class Event(
    val id: Long,
    val title: String,
    val description: String,
    val location: String,
    val startTime: Long,
    val endTime: Long,
    val isAllDay: Boolean,
    val reminder: Int // minutes before event
)