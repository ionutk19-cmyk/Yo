package com.example.calendarapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.calendarapp.databinding.ActivityAddEventBinding
import java.text.SimpleDateFormat
import java.util.*

class AddEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEventBinding
    private var eventId: Long = -1
    private var startDate: Calendar = Calendar.getInstance()
    private var endDate: Calendar = Calendar.getInstance()
    private var startTime: Calendar = Calendar.getInstance()
    private var endTime: Calendar = Calendar.getInstance()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupToolbar()
        setupDateTimePickers()
        setupReminderSpinner()
        loadEventData()
        setupClickListeners()
    }
    
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }
    
    private fun setupDateTimePickers() {
        // Initialize with current time
        val now = Calendar.getInstance()
        startDate = now.clone() as Calendar
        endDate = now.clone() as Calendar
        startTime = now.clone() as Calendar
        endTime = now.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 1) // Default 1 hour duration
        
        updateDateTimeButtons()
    }
    
    private fun setupReminderSpinner() {
        val reminderOptions = arrayOf(
            getString(R.string.no_reminder),
            "15 ${getString(R.string.minutes_before)}",
            "30 ${getString(R.string.minutes_before)}",
            "1 ${getString(R.string.hour_before)}",
            "1 ${getString(R.string.day_before)}",
            "1 ${getString(R.string.week_before)}"
        )
        
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, reminderOptions)
        binding.spinnerReminder.setAdapter(adapter)
        binding.spinnerReminder.setText(reminderOptions[0], false)
    }
    
    private fun loadEventData() {
        intent.getLongExtra("event_id", -1).let { id ->
            if (id != -1L) {
                eventId = id
                // In a real app, load event from database
                // For demo, we'll create a sample event
                loadSampleEvent()
            } else {
                // New event - check if we have a selected date
                intent.getLongExtra("selected_date", -1).let { selectedDate ->
                    if (selectedDate != -1L) {
                        val calendar = Calendar.getInstance()
                        calendar.timeInMillis = selectedDate
                        startDate = calendar.clone() as Calendar
                        endDate = calendar.clone() as Calendar
                        startTime = calendar.clone() as Calendar
                        endTime = calendar.clone() as Calendar
                        endTime.add(Calendar.HOUR_OF_DAY, 1)
                        updateDateTimeButtons()
                    }
                }
            }
        }
    }
    
    private fun loadSampleEvent() {
        // Load sample event data
        binding.etEventTitle.setText("Sample Event")
        binding.etEventDescription.setText("This is a sample event description")
        binding.etEventLocation.setText("Sample Location")
        binding.switchAllDay.isChecked = false
        
        // Set sample date/time
        startDate = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 1) }
        endDate = startDate.clone() as Calendar
        startTime = startDate.clone() as Calendar
        startTime.set(Calendar.HOUR_OF_DAY, 10)
        startTime.set(Calendar.MINUTE, 0)
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 1)
        
        updateDateTimeButtons()
    }
    
    private fun setupClickListeners() {
        binding.btnStartDate.setOnClickListener {
            showDatePicker(startDate) { calendar ->
                startDate = calendar
                updateDateTimeButtons()
            }
        }
        
        binding.btnEndDate.setOnClickListener {
            showDatePicker(endDate) { calendar ->
                endDate = calendar
                updateDateTimeButtons()
            }
        }
        
        binding.btnStartTime.setOnClickListener {
            showTimePicker(startTime) { calendar ->
                startTime = calendar
                updateDateTimeButtons()
            }
        }
        
        binding.btnEndTime.setOnClickListener {
            showTimePicker(endTime) { calendar ->
                endTime = calendar
                updateDateTimeButtons()
            }
        }
        
        binding.btnSave.setOnClickListener {
            saveEvent()
        }
        
        binding.btnCancel.setOnClickListener {
            finish()
        }
        
        binding.switchAllDay.setOnCheckedChangeListener { _, isChecked ->
            binding.btnStartTime.isEnabled = !isChecked
            binding.btnEndTime.isEnabled = !isChecked
        }
    }
    
    private fun showDatePicker(calendar: Calendar, onDateSelected: (Calendar) -> Unit) {
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                onDateSelected(calendar)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
    
    private fun showTimePicker(calendar: Calendar, onTimeSelected: (Calendar) -> Unit) {
        TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                onTimeSelected(calendar)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }
    
    private fun updateDateTimeButtons() {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        
        binding.btnStartDate.text = dateFormat.format(startDate.time)
        binding.btnEndDate.text = dateFormat.format(endDate.time)
        binding.btnStartTime.text = timeFormat.format(startTime.time)
        binding.btnEndTime.text = timeFormat.format(endTime.time)
    }
    
    private fun saveEvent() {
        val title = binding.etEventTitle.text.toString().trim()
        val description = binding.etEventDescription.text.toString().trim()
        val location = binding.etEventLocation.text.toString().trim()
        val isAllDay = binding.switchAllDay.isChecked
        
        if (title.isEmpty()) {
            binding.etEventTitle.error = "Title is required"
            return
        }
        
        // Create event
        val event = Event(
            id = if (eventId == -1L) System.currentTimeMillis() else eventId,
            title = title,
            description = description,
            location = location,
            startTime = if (isAllDay) startDate.timeInMillis else startTime.timeInMillis,
            endTime = if (isAllDay) endDate.timeInMillis else endTime.timeInMillis,
            isAllDay = isAllDay,
            reminder = getReminderMinutes()
        )
        
        // In a real app, save to database
        Toast.makeText(this, R.string.event_saved, Toast.LENGTH_SHORT).show()
        finish()
    }
    
    private fun getReminderMinutes(): Int {
        val reminderText = binding.spinnerReminder.text.toString()
        return when {
            reminderText.contains("15") -> 15
            reminderText.contains("30") -> 30
            reminderText.contains("hour") -> 60
            reminderText.contains("day") -> 1440
            reminderText.contains("week") -> 10080
            else -> 0
        }
    }
    
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (eventId != -1L) {
            menuInflater.inflate(R.menu.menu_add_event, menu)
        }
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                showDeleteConfirmation()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    private fun showDeleteConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Delete Event")
            .setMessage("Are you sure you want to delete this event?")
            .setPositiveButton("Delete") { _, _ ->
                // In a real app, delete from database
                Toast.makeText(this, R.string.event_deleted, Toast.LENGTH_SHORT).show()
                finish()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}