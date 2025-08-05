package com.example.calendarapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calendarapp.adapter.EventsAdapter
import com.example.calendarapp.data.Event
import com.example.calendarapp.databinding.ActivityMainBinding
import com.example.calendarapp.viewmodel.CalendarViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), OnDateSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: CalendarViewModel
    private lateinit var eventsAdapter: EventsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupViewModel()
        setupCalendar()
        setupEventsList()
        setupFab()
        
        observeViewModel()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[CalendarViewModel::class.java]
    }

    private fun setupCalendar() {
        binding.calendarView.setOnDateChangedListener(this)
        binding.calendarView.selectedDate = CalendarDay.today()
        
        // Set today button click listener
        binding.btnToday.setOnClickListener {
            val today = CalendarDay.today()
            binding.calendarView.selectedDate = today
            viewModel.selectDate(today.date)
        }
    }

    private fun setupEventsList() {
        eventsAdapter = EventsAdapter(
            onEventClick = { event ->
                openEventDetail(event)
            },
            onEventOptionsClick = { event, view ->
                showEventOptions(event, view)
            }
        )
        
        binding.eventsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = eventsAdapter
        }
    }

    private fun setupFab() {
        binding.fabAddEvent.setOnClickListener {
            val intent = Intent(this, AddEditEventActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeViewModel() {
        viewModel.selectedDate.observe(this) { date ->
            updateSelectedDateText(date)
            viewModel.getEventsForDate(date).observe(this) { events ->
                updateEventsList(events)
            }
        }
    }

    private fun updateSelectedDateText(date: Date) {
        val dateFormat = SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault())
        binding.selectedDateText.text = dateFormat.format(date)
    }

    private fun updateEventsList(events: List<Event>) {
        if (events.isEmpty()) {
            binding.eventsRecyclerView.visibility = View.GONE
            binding.noEventsText.visibility = View.VISIBLE
        } else {
            binding.eventsRecyclerView.visibility = View.VISIBLE
            binding.noEventsText.visibility = View.GONE
            eventsAdapter.submitList(events)
        }
    }

    private fun openEventDetail(event: Event) {
        val intent = Intent(this, EventDetailActivity::class.java)
        intent.putExtra("event_id", event.id)
        startActivity(intent)
    }

    private fun showEventOptions(event: Event, view: View) {
        val popup = PopupMenu(this, view)
        popup.menuInflater.inflate(R.menu.event_options_menu, popup.menu)
        
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_edit -> {
                    editEvent(event)
                    true
                }
                R.id.action_delete -> {
                    deleteEvent(event)
                    true
                }
                else -> false
            }
        }
        
        popup.show()
    }

    private fun editEvent(event: Event) {
        val intent = Intent(this, AddEditEventActivity::class.java)
        intent.putExtra("event_id", event.id)
        startActivity(intent)
    }

    private fun deleteEvent(event: Event) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle(getString(R.string.delete_event))
            .setMessage(getString(R.string.confirm_delete))
            .setPositiveButton(getString(R.string.delete)) { _, _ ->
                viewModel.deleteEvent(event)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    override fun onDateSelected(
        widget: MaterialCalendarView,
        date: CalendarDay,
        selected: Boolean
    ) {
        viewModel.selectDate(date.date)
    }
}