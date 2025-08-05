package com.example.calendarapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.calendarapp.data.CalendarDatabase
import com.example.calendarapp.data.Event
import com.example.calendarapp.repository.EventRepository
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class CalendarViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: EventRepository
    
    private val _selectedDate = MutableLiveData<Date>()
    val selectedDate: LiveData<Date> = _selectedDate

    private val _currentMonth = MutableLiveData<Date>()
    val currentMonth: LiveData<Date> = _currentMonth

    val allEvents: LiveData<List<Event>>
    val eventsForSelectedDate: LiveData<List<Event>>

    init {
        val eventDao = CalendarDatabase.getDatabase(application).eventDao()
        repository = EventRepository(eventDao)
        allEvents = repository.getAllEvents()
        
        // Initialize with today's date
        val today = Date()
        _selectedDate.value = today
        _currentMonth.value = today
        
        eventsForSelectedDate = repository.getEventsForDate(today)
    }

    fun selectDate(date: Date) {
        _selectedDate.value = date
    }

    fun setCurrentMonth(date: Date) {
        _currentMonth.value = date
    }

    fun getEventsForDate(date: Date): LiveData<List<Event>> {
        return repository.getEventsForDate(date)
    }

    fun getEventsInRange(startDate: Date, endDate: Date): LiveData<List<Event>> {
        return repository.getEventsInRange(startDate, endDate)
    }

    fun searchEvents(query: String): LiveData<List<Event>> {
        return repository.searchEvents(query)
    }

    fun insertEvent(event: Event, callback: (Long) -> Unit = {}) {
        viewModelScope.launch {
            val id = repository.insertEvent(event)
            callback(id)
        }
    }

    fun updateEvent(event: Event, callback: () -> Unit = {}) {
        viewModelScope.launch {
            repository.updateEvent(event)
            callback()
        }
    }

    fun deleteEvent(event: Event, callback: () -> Unit = {}) {
        viewModelScope.launch {
            repository.deleteEvent(event)
            callback()
        }
    }

    fun deleteEventById(id: Long, callback: () -> Unit = {}) {
        viewModelScope.launch {
            repository.deleteEventById(id)
            callback()
        }
    }

    suspend fun getEventById(id: Long): Event? {
        return repository.getEventById(id)
    }

    suspend fun getEventCountForDate(date: Date): Int {
        return repository.getEventCountForDate(date)
    }

    fun getNextMonth(): Date {
        val calendar = Calendar.getInstance()
        calendar.time = _currentMonth.value ?: Date()
        calendar.add(Calendar.MONTH, 1)
        return calendar.time
    }

    fun getPreviousMonth(): Date {
        val calendar = Calendar.getInstance()
        calendar.time = _currentMonth.value ?: Date()
        calendar.add(Calendar.MONTH, -1)
        return calendar.time
    }
}