package com.example.mobile_app.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface EventDao {
    @Insert
    suspend fun insertEvent(event: UnsentEvent): Long

    @Query("SELECT * FROM event_log ORDER BY id ASC")
    suspend fun getAllEvents(): List<UnsentEvent>

    // Выбираем только неотправленные события
    @Query("SELECT * FROM event_log WHERE isSent = 0 ORDER BY id ASC")
    suspend fun getAllUnsentEvents(): List<UnsentEvent>

    @Delete
    suspend fun deleteEvent(event: UnsentEvent)

    // Обновляем событие, помечая его как отправленное
    @Query("UPDATE event_log SET isSent = 1 WHERE id = :id")
    suspend fun markEventAsSent(id: Long)

    @Query("DELETE FROM event_log WHERE timestamp = :timestamp AND eventType = :eventType")
    suspend fun deleteEventByCriteria(timestamp: String, eventType: String)

    @Query("DELETE FROM event_log WHERE timestamp < :olderThan")
    suspend fun deleteEventsOlderThan(olderThan: String)
}