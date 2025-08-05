package com.example.calendarapp.ui.events

import androidx.lifecycle.ViewModel
import com.example.calendarapp.data.Event
import com.example.calendarapp.repository.EventRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted

class EventsViewModel(
    private val repository: EventRepository
) : ViewModel() {

    val events: StateFlow<List<Event>> = repository.getAllEvents()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}