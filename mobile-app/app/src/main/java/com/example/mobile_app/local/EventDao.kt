package com.example.mobile_app.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface EventDao {
    @Insert
    suspend fun insertEvent(event: UnsentEvent): Long

    @Query("SELECT * FROM unsent_events ORDER BY id ASC")
    suspend fun getAllEvents(): List<UnsentEvent>

    @Delete
    suspend fun deleteEvent(event: UnsentEvent)

    @Query("DELETE FROM unsent_events WHERE timestamp = :timestamp AND eventType = :eventType")
    suspend fun deleteEventByCriteria(timestamp: String, eventType: String)

    @Query("DELETE FROM unsent_events WHERE timestamp < :olderThan")
    suspend fun deleteEventsOlderThan(olderThan: String)
}