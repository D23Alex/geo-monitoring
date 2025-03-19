package com.example.mobile_app.network

import com.example.mobile_app.model.Event
import com.example.mobile_app.model.EventResponse
import com.example.mobile_app.model.SystemState
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface APIService {
    @POST("/api/events")
    suspend fun sendEvent(@Body event: Event): Response<EventResponse>

    @GET("/api/current-state")
    suspend fun getCurrentState(): Response<SystemState>
}
