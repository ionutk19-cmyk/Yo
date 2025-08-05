package com.example.calendarapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime

@Dao
interface EventDao {
    
    @Query("SELECT * FROM events ORDER BY startDateTime ASC")
    fun getAllEvents(): Flow<List<Event>>
    
    @Query("SELECT * FROM events WHERE startDateTime >= :startDate AND startDateTime < :endDate ORDER BY startDateTime ASC")
    fun getEventsBetweenDates(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<Event>>
    
    @Query("SELECT * FROM events WHERE DATE(startDateTime) = DATE(:date) ORDER BY startDateTime ASC")
    fun getEventsForDate(date: LocalDate): Flow<List<Event>>
    
    @Query("SELECT * FROM events WHERE startDateTime >= :startDate ORDER BY startDateTime ASC LIMIT :limit")
    fun getUpcomingEvents(startDate: LocalDateTime, limit: Int = 10): Flow<List<Event>>
    
    @Query("SELECT * FROM events WHERE id = :eventId")
    suspend fun getEventById(eventId: Long): Event?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event): Long
    
    @Update
    suspend fun updateEvent(event: Event)
    
    @Delete
    suspend fun deleteEvent(event: Event)
    
    @Query("DELETE FROM events WHERE id = :eventId")
    suspend fun deleteEventById(eventId: Long)
    
    @Query("SELECT COUNT(*) FROM events WHERE DATE(startDateTime) = DATE(:date)")
    suspend fun getEventCountForDate(date: LocalDate): Int
    
    @Query("SELECT * FROM events WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' ORDER BY startDateTime ASC")
    fun searchEvents(query: String): Flow<List<Event>>
}