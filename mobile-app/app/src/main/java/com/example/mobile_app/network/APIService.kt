package com.example.mobile_app.network

import com.example.mobile_app.model.Event
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface APIService {
    @POST("/api/events")
    fun sendEvent(@Body event: Event): Call<Void>
    // Дополнительные API запросы (авторизация, регистрация и т.д.) можно добавить здесь
}
