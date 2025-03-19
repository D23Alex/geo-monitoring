package com.example.mobile_app.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mobile_app.model.SystemState

@Entity(tableName = "system_state")
data class CachedSystemState(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    // В данном примере timestamp хранится как строка в формате ISO-8601
    val timestamp: String,
    val stateJson: String
)

fun CachedSystemState.toSystemState(): SystemState {
    val gson = com.google.gson.Gson()
    return gson.fromJson(this.stateJson, SystemState::class.java)
}