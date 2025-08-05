package com.example.calendarapp.ui.calendar.dialog

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.calendarapp.R
import com.example.calendarapp.data.Event
import com.example.calendarapp.databinding.DialogAddEventBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class AddEventDialog : DialogFragment() {

    private var _binding: DialogAddEventBinding? = null
    private val binding get() = _binding!!
    
    private var event: Event? = null
    private var selectedDate: LocalDate = LocalDate.now()
    private var selectedStartTime: LocalTime = LocalTime.now()
    private var selectedEndTime: LocalTime = LocalTime.now().plusHours(1)
    private var selectedReminder: Int = 15

    companion object {
        private const val ARG_EVENT = "event"
        
        fun newInstance(event: Event? = null): AddEventDialog {
            return AddEventDialog().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_EVENT, event)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAddEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        event = arguments?.getParcelable(ARG_EVENT)
        setupViews()
        setupClickListeners()
        
        if (event != null) {
            loadEventData()
        }
    }

    private fun setupViews() {
        // Set default date and time
        updateDateDisplay()
        updateTimeDisplay()
        updateReminderDisplay()
    }

    private fun setupClickListeners() {
        binding.etEventDate.setOnClickListener {
            showDatePicker()
        }
        
        binding.etEventTime.setOnClickListener {
            showTimePicker()
        }
        
        binding.etEventReminder.setOnClickListener {
            showReminderPicker()
        }
        
        binding.btnSaveEvent.setOnClickListener {
            saveEvent()
        }
        
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun loadEventData() {
        event?.let { event ->
            binding.etEventTitle.setText(event.title)
            binding.etEventDescription.setText(event.description)
            binding.etEventLocation.setText(event.location)
            
            selectedDate = event.startDateTime.toLocalDate()
            selectedStartTime = event.startDateTime.toLocalTime()
            selectedEndTime = event.endDateTime.toLocalTime()
            selectedReminder = event.reminderMinutes
            
            updateDateDisplay()
            updateTimeDisplay()
            updateReminderDisplay()
        }
    }

    private fun showDatePicker() {
        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                updateDateDisplay()
            },
            selectedDate.year,
            selectedDate.monthValue - 1,
            selectedDate.dayOfMonth
        ).show()
    }

    private fun showTimePicker() {
        TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                selectedStartTime = LocalTime.of(hourOfDay, minute)
                selectedEndTime = selectedStartTime.plusHours(1)
                updateTimeDisplay()
            },
            selectedStartTime.hour,
            selectedStartTime.minute,
            false
        ).show()
    }

    private fun showReminderPicker() {
        val reminderOptions = arrayOf(
            getString(R.string.no_reminder),
            getString(R.string.reminder_5_min),
            getString(R.string.reminder_15_min),
            getString(R.string.reminder_30_min),
            getString(R.string.reminder_1_hour),
            getString(R.string.reminder_1_day)
        )
        
        val reminderValues = intArrayOf(0, 5, 15, 30, 60, 1440)
        
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.event_reminder)
            .setItems(reminderOptions) { _, which ->
                selectedReminder = reminderValues[which]
                updateReminderDisplay()
            }
            .show()
    }

    private fun updateDateDisplay() {
        val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
        binding.etEventDate.setText(selectedDate.format(formatter))
    }

    private fun updateTimeDisplay() {
        val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")
        val startTime = selectedStartTime.format(timeFormatter)
        val endTime = selectedEndTime.format(timeFormatter)
        binding.etEventTime.setText("$startTime - $endTime")
    }

    private fun updateReminderDisplay() {
        val reminderText = when (selectedReminder) {
            0 -> getString(R.string.no_reminder)
            5 -> getString(R.string.reminder_5_min)
            15 -> getString(R.string.reminder_15_min)
            30 -> getString(R.string.reminder_30_min)
            60 -> getString(R.string.reminder_1_hour)
            1440 -> getString(R.string.reminder_1_day)
            else -> getString(R.string.reminder_15_min)
        }
        binding.etEventReminder.setText(reminderText)
    }

    private fun saveEvent() {
        val title = binding.etEventTitle.text.toString().trim()
        if (title.isEmpty()) {
            binding.etEventTitle.error = "Title is required"
            return
        }
        
        val description = binding.etEventDescription.text.toString().trim()
        val location = binding.etEventLocation.text.toString().trim()
        
        val startDateTime = LocalDateTime.of(selectedDate, selectedStartTime)
        val endDateTime = LocalDateTime.of(selectedDate, selectedEndTime)
        
        val newEvent = Event(
            id = event?.id ?: 0,
            title = title,
            description = if (description.isNotEmpty()) description else null,
            startDateTime = startDateTime,
            endDateTime = endDateTime,
            location = if (location.isNotEmpty()) location else null,
            reminderMinutes = selectedReminder
        )
        
        // TODO: Save event through ViewModel
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}