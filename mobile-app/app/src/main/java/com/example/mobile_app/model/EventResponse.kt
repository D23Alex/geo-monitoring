package com.example.mobile_app.model

import com.google.gson.annotations.SerializedName

data class EventResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("eventType") val eventType: String,
    // Дополнительные поля в зависимости от типа события
)