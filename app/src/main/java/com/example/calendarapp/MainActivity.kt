package com.example.calendarapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var selectedDate: Long = System.currentTimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        val btnAddEvent = findViewById<Button>(R.id.btnAddEvent)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = java.util.Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            selectedDate = calendar.timeInMillis
        }

        btnAddEvent.setOnClickListener {
            val intent = Intent(this, AddEventActivity::class.java)
            intent.putExtra(AddEventActivity.EXTRA_DATE_MILLIS, selectedDate)
            startActivity(intent)
        }
    }
}