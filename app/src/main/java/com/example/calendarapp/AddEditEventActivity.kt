package com.example.calendarapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calendarapp.adapter.ColorAdapter
import com.example.calendarapp.data.Event
import com.example.calendarapp.databinding.ActivityAddEditEventBinding
import com.example.calendarapp.viewmodel.CalendarViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddEditEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditEventBinding
    private lateinit var viewModel: CalendarViewModel
    private lateinit var colorAdapter: ColorAdapter
    
    private var editingEventId: Long = -1
    private var editingEvent: Event? = null
    private var startDateTime = Calendar.getInstance()
    private var endDateTime = Calendar.getInstance()
    private var selectedColor = 0xFF2196F3.toInt()
    
    private val eventColors = listOf(
        0xFF2196F3.toInt(), // Blue
        0xFF4CAF50.toInt(), // Green
        0xFFFF9800.toInt(), // Orange
        0xFFF44336.toInt(), // Red
        0xFF9C27B0.toInt(), // Purple
        0xFF009688.toInt(), // Teal
        0xFFFF5722.toInt(), // Deep Orange
        0xFF795548.toInt()  // Brown
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupViewModel()
        setupColorAdapter()
        setupDateTimeButtons()
        setupAllDayToggle()
        setupActionButtons()
        
        loadEventForEditing()
        initializeDateTime()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[CalendarViewModel::class.java]
    }

    private fun setupColorAdapter() {
        colorAdapter = ColorAdapter(eventColors) { color ->
            selectedColor = color
        }
        
        binding.colorRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@AddEditEventActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = colorAdapter
        }
        
        colorAdapter.setSelectedColor(selectedColor)
    }

    private fun setupDateTimeButtons() {
        binding.btnStartDate.setOnClickListener { showDatePicker(true) }
        binding.btnStartTime.setOnClickListener { showTimePicker(true) }
        binding.btnEndDate.setOnClickListener { showDatePicker(false) }
        binding.btnEndTime.setOnClickListener { showTimePicker(false) }
    }

    private fun setupAllDayToggle() {
        binding.switchAllDay.setOnCheckedChangeListener { _, isChecked ->
            binding.dateTimeSection.visibility = if (isChecked) View.GONE else View.VISIBLE
            
            if (isChecked) {
                // Set times to full day
                startDateTime.set(Calendar.HOUR_OF_DAY, 0)
                startDateTime.set(Calendar.MINUTE, 0)
                endDateTime.set(Calendar.HOUR_OF_DAY, 23)
                endDateTime.set(Calendar.MINUTE, 59)
            }
            
            updateDateTimeButtons()
        }
    }

    private fun setupActionButtons() {
        binding.btnCancel.setOnClickListener { finish() }
        binding.btnSave.setOnClickListener { saveEvent() }
    }

    private fun loadEventForEditing() {
        editingEventId = intent.getLongExtra("event_id", -1)
        
        if (editingEventId != -1L) {
            binding.toolbar.title = getString(R.string.edit_event)
            
            lifecycleScope.launch {
                editingEvent = viewModel.getEventById(editingEventId)
                editingEvent?.let { event ->
                    populateEventData(event)
                }
            }
        }
    }

    private fun populateEventData(event: Event) {
        binding.editTitle.setText(event.title)
        binding.editDescription.setText(event.description)
        binding.editLocation.setText(event.location)
        binding.switchAllDay.isChecked = event.isAllDay
        
        startDateTime.time = event.startTime
        endDateTime.time = event.endTime
        selectedColor = event.color
        
        colorAdapter.setSelectedColor(selectedColor)
        updateDateTimeButtons()
    }

    private fun initializeDateTime() {
        if (editingEventId == -1L) {
            // For new events, set end time 1 hour after start time
            endDateTime.time = startDateTime.time
            endDateTime.add(Calendar.HOUR_OF_DAY, 1)
        }
        
        updateDateTimeButtons()
    }

    private fun updateDateTimeButtons() {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        
        binding.btnStartDate.text = dateFormat.format(startDateTime.time)
        binding.btnStartTime.text = timeFormat.format(startDateTime.time)
        binding.btnEndDate.text = dateFormat.format(endDateTime.time)
        binding.btnEndTime.text = timeFormat.format(endDateTime.time)
    }

    private fun showDatePicker(isStartDate: Boolean) {
        val calendar = if (isStartDate) startDateTime else endDateTime
        
        DatePickerDialog(
            this,
            { _, year, month, day ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)
                updateDateTimeButtons()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showTimePicker(isStartTime: Boolean) {
        val calendar = if (isStartTime) startDateTime else endDateTime
        
        TimePickerDialog(
            this,
            { _, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                updateDateTimeButtons()
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun saveEvent() {
        val title = binding.editTitle.text.toString().trim()
        
        if (title.isEmpty()) {
            binding.editTitle.error = getString(R.string.please_enter_title)
            return
        }

        val description = binding.editDescription.text.toString().trim()
        val location = binding.editLocation.text.toString().trim()
        val isAllDay = binding.switchAllDay.isChecked

        val event = if (editingEventId == -1L) {
            // Create new event
            Event(
                title = title,
                description = description.ifEmpty { null },
                location = location.ifEmpty { null },
                startTime = startDateTime.time,
                endTime = endDateTime.time,
                isAllDay = isAllDay,
                color = selectedColor
            )
        } else {
            // Update existing event
            editingEvent!!.copy(
                title = title,
                description = description.ifEmpty { null },
                location = location.ifEmpty { null },
                startTime = startDateTime.time,
                endTime = endDateTime.time,
                isAllDay = isAllDay,
                color = selectedColor,
                updatedAt = Date()
            )
        }

        if (editingEventId == -1L) {
            viewModel.insertEvent(event) {
                runOnUiThread {
                    Toast.makeText(this, getString(R.string.event_saved), Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        } else {
            viewModel.updateEvent(event) {
                runOnUiThread {
                    Toast.makeText(this, getString(R.string.event_updated), Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}