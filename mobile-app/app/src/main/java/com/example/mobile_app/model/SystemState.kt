package com.example.mobile_app.model

import com.example.mobile_app.local.CachedSystemState

data class SystemState(
    val lastEvent: Event? = null
) {
    fun withLastEvent(event: Event): SystemState = copy(lastEvent = event)
}

fun SystemState.toCachedState(): CachedSystemState {
    // Здесь используется, например, Gson для сериализации SystemState в JSON-строку
    val gson = com.google.gson.Gson()
    return CachedSystemState(
        timestamp = this.lastEvent?.timestamp ?: "",
        stateJson = gson.toJson(this)
    )
}