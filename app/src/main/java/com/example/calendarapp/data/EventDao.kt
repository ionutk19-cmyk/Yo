package com.example.calendarapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.Date

@Dao
interface EventDao {
    @Query("SELECT * FROM events ORDER BY startTime ASC")
    fun getAllEvents(): LiveData<List<Event>>

    @Query("SELECT * FROM events WHERE id = :id")
    suspend fun getEventById(id: Long): Event?

    @Query("SELECT * FROM events WHERE DATE(startTime/1000, 'unixepoch') = DATE(:date/1000, 'unixepoch') ORDER BY startTime ASC")
    fun getEventsForDate(date: Long): LiveData<List<Event>>

    @Query("SELECT * FROM events WHERE startTime >= :startDate AND startTime <= :endDate ORDER BY startTime ASC")
    fun getEventsInRange(startDate: Long, endDate: Long): LiveData<List<Event>>

    @Query("SELECT * FROM events WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' ORDER BY startTime ASC")
    fun searchEvents(query: String): LiveData<List<Event>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event): Long

    @Update
    suspend fun updateEvent(event: Event)

    @Delete
    suspend fun deleteEvent(event: Event)

    @Query("DELETE FROM events WHERE id = :id")
    suspend fun deleteEventById(id: Long)

    @Query("SELECT COUNT(*) FROM events WHERE DATE(startTime/1000, 'unixepoch') = DATE(:date/1000, 'unixepoch')")
    suspend fun getEventCountForDate(date: Long): Int
}