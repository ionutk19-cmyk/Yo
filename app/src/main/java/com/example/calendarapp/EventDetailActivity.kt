package com.example.calendarapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.calendarapp.data.Event
import com.example.calendarapp.databinding.ActivityEventDetailBinding
import com.example.calendarapp.viewmodel.CalendarViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class EventDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEventDetailBinding
    private lateinit var viewModel: CalendarViewModel
    private var currentEvent: Event? = null
    private var eventId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupViewModel()
        loadEvent()
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

    private fun loadEvent() {
        eventId = intent.getLongExtra("event_id", -1)
        
        if (eventId != -1L) {
            lifecycleScope.launch {
                currentEvent = viewModel.getEventById(eventId)
                currentEvent?.let { event ->
                    displayEventDetails(event)
                }
            }
        }
    }

    private fun displayEventDetails(event: Event) {
        binding.eventTitle.text = event.title
        
        // Format and display date and time
        val dateFormat = SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        
        binding.eventDate.text = dateFormat.format(event.startTime)
        
        if (event.isAllDay) {
            binding.eventTime.text = "All day"
        } else {
            val startTime = timeFormat.format(event.startTime)
            val endTime = timeFormat.format(event.endTime)
            binding.eventTime.text = "$startTime - $endTime"
        }
        
        // Location
        if (event.location.isNullOrBlank()) {
            binding.locationCard.visibility = android.view.View.GONE
        } else {
            binding.locationCard.visibility = android.view.View.VISIBLE
            binding.eventLocation.text = event.location
        }
        
        // Description
        if (event.description.isNullOrBlank()) {
            binding.descriptionCard.visibility = android.view.View.GONE
        } else {
            binding.descriptionCard.visibility = android.view.View.VISIBLE
            binding.eventDescription.text = event.description
        }
        
        // Color indicator
        binding.eventColorIndicator.setBackgroundColor(event.color)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.event_detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_edit -> {
                editEvent()
                true
            }
            R.id.action_delete -> {
                deleteEvent()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun editEvent() {
        currentEvent?.let { event ->
            val intent = Intent(this, AddEditEventActivity::class.java)
            intent.putExtra("event_id", event.id)
            startActivity(intent)
        }
    }

    private fun deleteEvent() {
        currentEvent?.let { event ->
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(getString(R.string.delete_event))
                .setMessage(getString(R.string.confirm_delete))
                .setPositiveButton(getString(R.string.delete)) { _, _ ->
                    viewModel.deleteEvent(event) {
                        runOnUiThread {
                            Toast.makeText(this, getString(R.string.event_deleted), Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                }
                .setNegativeButton(getString(R.string.cancel), null)
                .show()
        }
    }

    override fun onResume() {
        super.onResume()
        // Reload event in case it was edited
        loadEvent()
    }
}