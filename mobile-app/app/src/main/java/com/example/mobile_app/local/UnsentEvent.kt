package com.example.mobile_app.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event_log")
data class UnsentEvent(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val eventJson: String, // сериализованное событие
    val timestamp: String, // можно использовать ISO-8601 формат
    val eventType: String,
    val signature: String,  // цифровая подпись HMAC
    val isSent: Boolean = false  // новое поле: true, если событие успешно отправлено
)