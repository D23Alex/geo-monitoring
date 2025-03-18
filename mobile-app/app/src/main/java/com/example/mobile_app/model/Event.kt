package com.example.mobile_app.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.annotations.SerializedName
import java.time.Instant

abstract class Event {
    @SerializedName("id")
    var id: Long? = null

    @RequiresApi(Build.VERSION_CODES.O)
    @SerializedName("timestamp")
    var timestamp: Instant = Instant.now()

    abstract fun apply(oldState: SystemState): SystemState

    fun updateState(oldState: SystemState): SystemState {
        return apply(oldState).withLastEvent(this)
    }
}

data class SystemState(
    val lastEvent: Event? = null
) {
    fun withLastEvent(event: Event): SystemState = copy(lastEvent = event)
}
