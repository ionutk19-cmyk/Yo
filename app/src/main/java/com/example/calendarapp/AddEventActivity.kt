package com.example.calendarapp

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddEventActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DATE_MILLIS = "date_millis"
        private const val PREF_EVENTS = "events"
    }

    private var dateMillis: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)

        dateMillis = intent.getLongExtra(EXTRA_DATE_MILLIS, 0L)

        val etEvent = findViewById<EditText>(R.id.etEvent)
        val btnSave = findViewById<Button>(R.id.btnSave)

        btnSave.setOnClickListener {
            val eventText = etEvent.text.toString()
            if (eventText.isNotBlank()) {
                saveEvent(eventText)
                Toast.makeText(this, "Event saved", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Please enter event details", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveEvent(event: String) {
        val prefs = getSharedPreferences(PREF_EVENTS, Context.MODE_PRIVATE)
        val key = dateMillis.toString()
        val existing = prefs.getStringSet(key, mutableSetOf()) ?: mutableSetOf()
        existing.add(event)
        prefs.edit().putStringSet(key, existing).apply()
    }
}