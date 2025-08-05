package com.example.calendarapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calendarapp.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var calendarAdapter: CalendarAdapter
    private lateinit var eventAdapter: EventAdapter
    private val calendar = Calendar.getInstance()
    private val events = mutableListOf<Event>()
    private var selectedDate: Calendar = Calendar.getInstance()

    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupCalendar()
        setupEventList()
        setupClickListeners()
        checkPermissions()
        loadEvents()
    }

    private fun setupCalendar() {
        calendarAdapter = CalendarAdapter { date ->
            selectedDate = date
            updateSelectedDate()
            loadEventsForDate(date)
        }
        
        binding.rvCalendar.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 7)
            adapter = calendarAdapter
        }
        
        updateCalendarView()
    }

    private fun setupEventList() {
        eventAdapter = EventAdapter { event ->
            // Open event detail
            val intent = Intent(this, AddEventActivity::class.java).apply {
                putExtra("event_id", event.id)
            }
            startActivity(intent)
        }
        
        binding.rvEvents.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = eventAdapter
        }
    }

    private fun setupClickListeners() {
        binding.btnPreviousMonth.setOnClickListener {
            calendar.add(Calendar.MONTH, -1)
            updateCalendarView()
        }

        binding.btnNextMonth.setOnClickListener {
            calendar.add(Calendar.MONTH, 1)
            updateCalendarView()
        }

        binding.fabAddEvent.setOnClickListener {
            val intent = Intent(this, AddEventActivity::class.java).apply {
                putExtra("selected_date", selectedDate.timeInMillis)
            }
            startActivity(intent)
        }
    }

    private fun updateCalendarView() {
        val monthYear = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
            .format(calendar.time)
        binding.tvCurrentMonth.text = monthYear
        
        calendarAdapter.updateCalendar(calendar)
    }

    private fun updateSelectedDate() {
        val dateFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
        binding.tvSelectedDate.text = dateFormat.format(selectedDate.time)
    }

    private fun loadEventsForDate(date: Calendar) {
        val dayEvents = events.filter { event ->
            val eventDate = Calendar.getInstance().apply {
                timeInMillis = event.startTime
            }
            eventDate.get(Calendar.YEAR) == date.get(Calendar.YEAR) &&
            eventDate.get(Calendar.MONTH) == date.get(Calendar.MONTH) &&
            eventDate.get(Calendar.DAY_OF_MONTH) == date.get(Calendar.DAY_OF_MONTH)
        }
        
        eventAdapter.updateEvents(dayEvents)
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CALENDAR
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_CALENDAR
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_CALENDAR,
                    Manifest.permission.WRITE_CALENDAR
                ),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun loadEvents() {
        // In a real app, this would load from a database
        // For demo purposes, we'll create some sample events
        val sampleEvents = listOf(
            Event(
                id = 1L,
                title = "Team Meeting",
                description = "Weekly team sync",
                location = "Conference Room A",
                startTime = Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_MONTH, 1)
                    set(Calendar.HOUR_OF_DAY, 10)
                    set(Calendar.MINUTE, 0)
                }.timeInMillis,
                endTime = Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_MONTH, 1)
                    set(Calendar.HOUR_OF_DAY, 11)
                    set(Calendar.MINUTE, 0)
                }.timeInMillis,
                isAllDay = false,
                reminder = 15
            ),
            Event(
                id = 2L,
                title = "Doctor Appointment",
                description = "Annual checkup",
                location = "Medical Center",
                startTime = Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_MONTH, 2)
                    set(Calendar.HOUR_OF_DAY, 14)
                    set(Calendar.MINUTE, 30)
                }.timeInMillis,
                endTime = Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_MONTH, 2)
                    set(Calendar.HOUR_OF_DAY, 15)
                    set(Calendar.MINUTE, 30)
                }.timeInMillis,
                isAllDay = false,
                reminder = 60
            )
        )
        
        events.clear()
        events.addAll(sampleEvents)
        loadEventsForDate(selectedDate)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, R.string.permission_granted, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh events when returning from add/edit activity
        loadEventsForDate(selectedDate)
    }
}