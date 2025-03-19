package com.example.mobile_app.model

data class ClientData(
    val clientId: Long,
    val login: String,
    val password: String,
    val mac: String,
    val imei: String
)