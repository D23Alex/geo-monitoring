package com.example.mobile_app.network

import com.example.mobile_app.model.Event
import com.example.mobile_app.model.EventResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface APIService {
    @POST("/api/events")
    suspend fun sendEvent(@Body event: Event): Response<EventResponse>
}
