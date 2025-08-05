package com.example.calendarapp.repository

import com.example.calendarapp.data.Event
import com.example.calendarapp.data.EventDao
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime

class EventRepository(private val eventDao: EventDao) {
    
    fun getAllEvents(): Flow<List<Event>> = eventDao.getAllEvents()
    
    fun getEventsBetweenDates(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<Event>> =
        eventDao.getEventsBetweenDates(startDate, endDate)
    
    fun getEventsForDate(date: LocalDate): Flow<List<Event>> = eventDao.getEventsForDate(date)
    
    fun getUpcomingEvents(startDate: LocalDateTime, limit: Int = 10): Flow<List<Event>> =
        eventDao.getUpcomingEvents(startDate, limit)
    
    suspend fun getEventById(eventId: Long): Event? = eventDao.getEventById(eventId)
    
    suspend fun insertEvent(event: Event): Long = eventDao.insertEvent(event)
    
    suspend fun updateEvent(event: Event) = eventDao.updateEvent(event)
    
    suspend fun deleteEvent(event: Event) = eventDao.deleteEvent(event)
    
    suspend fun deleteEventById(eventId: Long) = eventDao.deleteEventById(eventId)
    
    suspend fun getEventCountForDate(date: LocalDate): Int = eventDao.getEventCountForDate(date)
    
    fun searchEvents(query: String): Flow<List<Event>> = eventDao.searchEvents(query)
}