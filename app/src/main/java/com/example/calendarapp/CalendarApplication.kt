package com.example.calendarapp

import android.app.Application
import com.example.calendarapp.data.AppDatabase

class CalendarApplication : Application() {
    
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
    
    override fun onCreate() {
        super.onCreate()
        // Initialize any app-wide configurations here
    }
}