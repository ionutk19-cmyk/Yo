package com.example.calendarapp.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendarapp.data.Event
import com.example.calendarapp.repository.EventRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import com.example.calendarapp.ui.calendar.model.CalendarDay

class CalendarViewModel(
    private val repository: EventRepository
) : ViewModel() {

    private val _currentMonth = MutableStateFlow(YearMonth.now())
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    
    val calendarDays: StateFlow<List<CalendarDay>> = _currentMonth
        .map { month -> generateCalendarDays(month) }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    
    val selectedDateEvents: StateFlow<List<Event>> = _selectedDate
        .flatMapLatest { date -> repository.getEventsForDate(date) }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun setCurrentMonth(month: YearMonth) {
        _currentMonth.value = month
    }

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun addEvent(event: Event) {
        viewModelScope.launch {
            repository.insertEvent(event)
        }
    }

    fun updateEvent(event: Event) {
        viewModelScope.launch {
            repository.updateEvent(event)
        }
    }

    fun deleteEvent(event: Event) {
        viewModelScope.launch {
            repository.deleteEvent(event)
        }
    }

    private fun generateCalendarDays(month: YearMonth): List<CalendarDay> {
        val days = mutableListOf<CalendarDay>()
        
        // Get the first day of the month
        val firstDayOfMonth = month.atDay(1)
        // Get the day of week for the first day (1 = Monday, 7 = Sunday)
        val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value
        // Calculate how many days from previous month to show
        val daysFromPreviousMonth = if (firstDayOfWeek == 7) 0 else firstDayOfWeek
        
        // Add days from previous month
        val previousMonth = month.minusMonths(1)
        val lastDayOfPreviousMonth = previousMonth.lengthOfMonth()
        for (i in daysFromPreviousMonth downTo 1) {
            val day = previousMonth.atDay(lastDayOfPreviousMonth - i + 1)
            days.add(CalendarDay(day, false, false, false, false))
        }
        
        // Add days from current month
        val today = LocalDate.now()
        for (dayOfMonth in 1..month.lengthOfMonth()) {
            val day = month.atDay(dayOfMonth)
            val isToday = day == today
            val isSelected = day == _selectedDate.value
            days.add(CalendarDay(day, true, isToday, isSelected, false))
        }
        
        // Add days from next month to complete the grid
        val remainingDays = 42 - days.size // 6 rows * 7 days = 42
        val nextMonth = month.plusMonths(1)
        for (dayOfMonth in 1..remainingDays) {
            val day = nextMonth.atDay(dayOfMonth)
            days.add(CalendarDay(day, false, false, false, false))
        }
        
        return days
    }
}

