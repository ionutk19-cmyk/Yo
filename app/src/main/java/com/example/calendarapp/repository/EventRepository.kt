package com.example.calendarapp.repository

import androidx.lifecycle.LiveData
import com.example.calendarapp.data.Event
import com.example.calendarapp.data.EventDao
import java.util.Date

class EventRepository(private val eventDao: EventDao) {

    fun getAllEvents(): LiveData<List<Event>> = eventDao.getAllEvents()

    suspend fun getEventById(id: Long): Event? = eventDao.getEventById(id)

    fun getEventsForDate(date: Date): LiveData<List<Event>> = 
        eventDao.getEventsForDate(date.time)

    fun getEventsInRange(startDate: Date, endDate: Date): LiveData<List<Event>> = 
        eventDao.getEventsInRange(startDate.time, endDate.time)

    fun searchEvents(query: String): LiveData<List<Event>> = eventDao.searchEvents(query)

    suspend fun insertEvent(event: Event): Long = eventDao.insertEvent(event)

    suspend fun updateEvent(event: Event) = eventDao.updateEvent(event)

    suspend fun deleteEvent(event: Event) = eventDao.deleteEvent(event)

    suspend fun deleteEventById(id: Long) = eventDao.deleteEventById(id)

    suspend fun getEventCountForDate(date: Date): Int = 
        eventDao.getEventCountForDate(date.time)
}