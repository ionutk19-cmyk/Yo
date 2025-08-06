package com.whatsapp.auto.responder.dialog

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.whatsapp.auto.responder.R
import com.whatsapp.auto.responder.databinding.DialogAddScheduledMessageBinding
import com.whatsapp.auto.responder.model.ScheduledMessage
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class AddScheduledMessageDialog(
    private val context: Context,
    private val scheduledMessage: ScheduledMessage? = null,
    private val onSave: (message: String, phoneNumber: String, time: LocalDateTime, repeatType: String) -> Unit
) : DialogFragment() {

    private var _binding: DialogAddScheduledMessageBinding? = null
    private val binding get() = _binding!!
    
    private var selectedDateTime: LocalDateTime = LocalDateTime.now()
    private val timeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAddScheduledMessageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupUI()
        loadExistingData()
    }

    private fun setupUI() {
        binding.btnSelectDateTime.setOnClickListener {
            showDateTimePicker()
        }

        binding.btnSave.setOnClickListener {
            saveScheduledMessage()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        // Setup repeat type spinner
        val repeatTypes = arrayOf("No Repeat", "Daily", "Weekly", "Monthly")
        val repeatTypeValues = arrayOf("none", "daily", "weekly", "monthly")
        
        binding.spinnerRepeatType.adapter = android.widget.ArrayAdapter(
            context,
            android.R.layout.simple_spinner_item,
            repeatTypes
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
    }

    private fun loadExistingData() {
        scheduledMessage?.let { message ->
            binding.etMessage.setText(message.message)
            binding.etPhoneNumber.setText(message.phoneNumber)
            selectedDateTime = message.scheduledTime
            updateDateTimeDisplay()
            
            // Set repeat type
            val repeatTypeIndex = when (message.repeatType) {
                "none" -> 0
                "daily" -> 1
                "weekly" -> 2
                "monthly" -> 3
                else -> 0
            }
            binding.spinnerRepeatType.setSelection(repeatTypeIndex)
        }
    }

    private fun showDateTimePicker() {
        val calendar = Calendar.getInstance()
        
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                TimePickerDialog(
                    context,
                    { _, hourOfDay, minute ->
                        selectedDateTime = LocalDateTime.of(year, month + 1, dayOfMonth, hourOfDay, minute)
                        updateDateTimeDisplay()
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                ).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateDateTimeDisplay() {
        binding.tvSelectedDateTime.text = selectedDateTime.format(timeFormatter)
    }

    private fun saveScheduledMessage() {
        val message = binding.etMessage.text.toString().trim()
        val phoneNumber = binding.etPhoneNumber.text.toString().trim()
        
        if (message.isEmpty()) {
            Toast.makeText(context, getString(R.string.error_empty_message), Toast.LENGTH_SHORT).show()
            return
        }
        
        if (phoneNumber.isEmpty()) {
            Toast.makeText(context, getString(R.string.error_invalid_phone), Toast.LENGTH_SHORT).show()
            return
        }
        
        if (selectedDateTime.isBefore(LocalDateTime.now())) {
            Toast.makeText(context, getString(R.string.error_invalid_time), Toast.LENGTH_SHORT).show()
            return
        }
        
        val repeatType = when (binding.spinnerRepeatType.selectedItemPosition) {
            0 -> "none"
            1 -> "daily"
            2 -> "weekly"
            3 -> "monthly"
            else -> "none"
        }
        
        onSave(message, phoneNumber, selectedDateTime, repeatType)
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}