package com.example.whatsautoreply

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.whatsautoreply.databinding.ActivityMainBinding
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val calendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAutoReplySection()
        setupScheduleSection()
    }

    private fun setupAutoReplySection() {
        // Save the auto-reply text in SharedPreferences for simplicity
        val prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        binding.etAutoReply.setText(prefs.getString("auto_reply", "I\'m currently busy, will get back to you soon!"))

        binding.btnSaveAutoReply.setOnClickListener {
            prefs.edit().putString("auto_reply", binding.etAutoReply.text.toString()).apply()
        }
    }

    private fun setupScheduleSection() {
        binding.btnPickDate.setOnClickListener {
            DatePickerDialog(this, { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                pickTime()
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun pickTime() {
        TimePickerDialog(this, { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, 0)
            scheduleMessage()
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
    }

    private fun scheduleMessage() {
        val message = binding.etScheduledMessage.text.toString()
        if (message.isBlank()) return

        val intent = Intent(this, ScheduleReceiver::class.java).apply {
            putExtra("message", message)
        }
        val pendingIntent = PendingIntent.getBroadcast(this, System.currentTimeMillis().toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

        // user feedback (toast)
        android.widget.Toast.makeText(this, "Message scheduled!", android.widget.Toast.LENGTH_SHORT).show()
    }
}