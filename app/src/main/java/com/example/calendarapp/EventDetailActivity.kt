package com.example.calendarapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.calendarapp.databinding.ActivityAddEventBinding
import java.text.SimpleDateFormat
import java.util.*

class EventDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEventBinding
    private var eventId: Long = -1
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupToolbar()
        loadEventData()
        disableEditing()
    }
    
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Event Details"
        
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }
    
    private fun loadEventData() {
        eventId = intent.getLongExtra("event_id", -1)
        if (eventId != -1L) {
            // In a real app, load event from database
            // For demo, we'll create a sample event
            loadSampleEvent()
        }
    }
    
    private fun loadSampleEvent() {
        // Load sample event data
        binding.etEventTitle.setText("Sample Event")
        binding.etEventDescription.setText("This is a sample event description")
        binding.etEventLocation.setText("Sample Location")
        binding.switchAllDay.isChecked = false
        
        // Set sample date/time
        val startDate = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 1) }
        val endDate = startDate.clone() as Calendar
        val startTime = startDate.clone() as Calendar
        startTime.set(Calendar.HOUR_OF_DAY, 10)
        startTime.set(Calendar.MINUTE, 0)
        val endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 1)
        
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        
        binding.btnStartDate.text = dateFormat.format(startDate.time)
        binding.btnEndDate.text = dateFormat.format(endDate.time)
        binding.btnStartTime.text = timeFormat.format(startTime.time)
        binding.btnEndTime.text = timeFormat.format(endTime.time)
        
        binding.spinnerReminder.setText("15 minutes before", false)
    }
    
    private fun disableEditing() {
        binding.etEventTitle.isEnabled = false
        binding.etEventDescription.isEnabled = false
        binding.etEventLocation.isEnabled = false
        binding.btnStartDate.isEnabled = false
        binding.btnEndDate.isEnabled = false
        binding.btnStartTime.isEnabled = false
        binding.btnEndTime.isEnabled = false
        binding.switchAllDay.isEnabled = false
        binding.spinnerReminder.isEnabled = false
        binding.btnSave.visibility = android.view.View.GONE
        binding.btnCancel.visibility = android.view.View.GONE
    }
    
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add_event, menu)
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
                finish()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}