package com.example.calendarapp.ui.calendar

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.calendarapp.R
import com.example.calendarapp.databinding.FragmentCalendarBinding
import com.example.calendarapp.ui.calendar.adapter.CalendarAdapter
import com.example.calendarapp.ui.calendar.adapter.EventAdapter
import com.example.calendarapp.ui.calendar.dialog.AddEventDialog
import com.example.calendarapp.ui.calendar.model.CalendarDay
import com.example.calendarapp.ui.ViewModelFactory
import com.example.calendarapp.repository.EventRepository
import com.example.calendarapp.CalendarApplication
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: CalendarViewModel by viewModels { 
        ViewModelFactory(EventRepository((requireActivity().application as CalendarApplication).database.eventDao()))
    }
    private lateinit var calendarAdapter: CalendarAdapter
    private lateinit var eventAdapter: EventAdapter
    
    private var currentMonth = YearMonth.now()
    private var selectedDate = LocalDate.now()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCalendar()
        setupEventList()
        setupObservers()
        setupClickListeners()
        updateMonthDisplay()
    }

    private fun setupCalendar() {
        calendarAdapter = CalendarAdapter { day ->
            selectedDate = day.date
            viewModel.selectDate(selectedDate)
            updateSelectedDateDisplay()
        }
        
        binding.recyclerCalendar.apply {
            layoutManager = GridLayoutManager(context, 7)
            adapter = calendarAdapter
        }
    }

    private fun setupEventList() {
        eventAdapter = EventAdapter { event ->
            // Handle event click - show edit dialog
            showEditEventDialog(event)
        }
        
        binding.recyclerEvents.apply {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            adapter = eventAdapter
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.calendarDays.collectLatest { days ->
                calendarAdapter.submitList(days)
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.selectedDateEvents.collectLatest { events ->
                eventAdapter.submitList(events)
                binding.tvNoEvents.visibility = if (events.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnPreviousMonth.setOnClickListener {
            currentMonth = currentMonth.minusMonths(1)
            viewModel.setCurrentMonth(currentMonth)
            updateMonthDisplay()
        }
        
        binding.btnNextMonth.setOnClickListener {
            currentMonth = currentMonth.plusMonths(1)
            viewModel.setCurrentMonth(currentMonth)
            updateMonthDisplay()
        }
        
        binding.btnToday.setOnClickListener {
            selectedDate = LocalDate.now()
            currentMonth = YearMonth.now()
            viewModel.setCurrentMonth(currentMonth)
            viewModel.selectDate(selectedDate)
            updateMonthDisplay()
            updateSelectedDateDisplay()
        }
    }

    private fun updateMonthDisplay() {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        binding.tvMonthYear.text = currentMonth.format(formatter)
    }

    private fun updateSelectedDateDisplay() {
        val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy")
        binding.tvSelectedDate.text = selectedDate.format(formatter)
    }

    private fun showEditEventDialog(event: com.example.calendarapp.data.Event) {
        AddEventDialog.newInstance(event).show(childFragmentManager, "edit_event_dialog")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}