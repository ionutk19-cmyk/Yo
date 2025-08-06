package com.whatsappautoresponder

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddScheduledMessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Simple implementation - just show a toast for now
        Toast.makeText(this, "Add Scheduled Message - Feature coming soon!", Toast.LENGTH_LONG).show()
        finish()
    }
}