package com.example.mobile_app.repository

import com.example.mobile_app.model.Event
import com.example.mobile_app.model.EventResponse
import com.example.mobile_app.model.SystemState
import com.example.mobile_app.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EventRepository {
    private val apiService = ApiClient.apiService

    // Отправка события на сервер
    suspend fun sendEvent(event: Event): Result<EventResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.sendEvent(event)
                if (response.isSuccessful) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Ошибка отправки: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    // Получение текущего состояния системы
    suspend fun getCurrentState(): Result<SystemState> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getCurrentState()
                if (response.isSuccessful) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Ошибка получения состояния: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
