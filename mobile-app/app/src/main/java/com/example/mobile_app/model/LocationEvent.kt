package com.example.mobile_app.model

import com.google.gson.annotations.SerializedName

data class LocationEvent(
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("accuracy") val accuracy: Float
) : Event() {
    override fun apply(oldState: SystemState): SystemState = oldState.withLastEvent(this)
}
