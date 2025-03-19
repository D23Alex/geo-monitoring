package com.example.mobile_app.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.mobile_app.local.LocalEventDatabase
import com.example.mobile_app.local.UnsentEvent
import com.example.mobile_app.local.toSystemState
import com.example.mobile_app.model.Event
import com.example.mobile_app.model.EventResponse
import com.example.mobile_app.model.SystemState
import com.example.mobile_app.model.toCachedState
import com.example.mobile_app.network.ApiClient
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EventRepository(private val context: Context) {
    private val apiService = ApiClient.apiService
    private val localDb = LocalEventDatabase.getInstance(context)
    private val eventDao = localDb.eventDao()
    private val gson = Gson()

    // Отправка события с сохранением локально и попыткой отправки при наличии сети
    suspend fun sendEvent(event: Event): Result<EventResponse> {
        // Сохраняем событие локально (при необходимости можно использовать шифрованное хранилище)
        val unsentEvent = UnsentEvent(
            eventJson = gson.toJson(event),
            timestamp = event.timestamp,
            eventType = event.eventType
        )
        eventDao.insertEvent(unsentEvent)

        return withContext(Dispatchers.IO) {
            if (isNetworkAvailable()) {
                try {
                    val response = apiService.sendEvent(event)
                    if (response.isSuccessful) {
                        // Если отправка успешна, удаляем событие из локального хранилища
                        deleteLocalEvent(event)
                        Result.success(response.body()!!)
                    } else {
                        Result.failure(Exception("Ошибка отправки: ${response.code()}"))
                    }
                } catch (e: Exception) {
                    // Если возникла ошибка (например, сбой сети), событие уже сохранено локально
                    Result.failure(e)
                }
            } else {
                // Сеть недоступна – возвращаем ошибку, уведомляя, что событие сохранено для последующей отправки
                Result.failure(Exception("Сеть недоступна. Событие сохранено локально и будет отправлено при восстановлении соединения."))
            }
        }
    }

    // Пытаемся отправить все сохранённые неотправленные события
    suspend fun resendUnsentEvents() {
        if (!isNetworkAvailable()) return

        val pendingEvents = eventDao.getAllEvents()
        for (unsent in pendingEvents) {
            try {
                val event = gson.fromJson(unsent.eventJson, Event::class.java)
                val response = apiService.sendEvent(event)
                if (response.isSuccessful) {
                    eventDao.deleteEvent(unsent)
                }
            } catch (e: Exception) {
                // Если отправка не удалась, пропускаем и попробуем позже
            }
        }
    }

    // Удаляем локальное событие по совпадению по timestamp и eventType (упрощённо)
    private suspend fun deleteLocalEvent(event: Event) {
        val pendingEvents = eventDao.getAllEvents().filter {
            it.eventType == event.eventType && it.timestamp == event.timestamp
        }
        pendingEvents.forEach { eventDao.deleteEvent(it) }
    }

    // Периодически создаём снапшот (например, удаляем события старше 1 дня)
    suspend fun createSnapshotAndCleanOldEvents() {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val cutoff = sdf.format(Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000))
        eventDao.deleteEventsOlderThan(cutoff)
        // Здесь можно сохранить текущее состояние в отдельной таблице (с использованием шифрования при необходимости)
    }

    // Получение текущего состояния системы с учетом оффлайн-кеша
    suspend fun getCurrentState(): Result<SystemState> {
        return withContext(Dispatchers.IO) {
            if (isNetworkAvailable()) {
                try {
                    val response = apiService.getCurrentState()
                    if (response.isSuccessful) {
                        val state = response.body()!!
                        // Сохраняем полученное состояние в локальный кэш
                        localDb.systemStateDao().insertState(state.toCachedState())
                        Result.success(state)
                    } else {
                        // Если сервер вернул ошибку, пытаемся загрузить последнее кэшированное состояние
                        localDb.systemStateDao().getLatestState()?.let { cached ->
                            Result.success(cached.toSystemState())
                        } ?: Result.failure(Exception("Ошибка получения состояния: ${response.code()}"))
                    }
                } catch (e: Exception) {
                    // В случае ошибки сети возвращаем кэшированное состояние, если оно имеется
                    localDb.systemStateDao().getLatestState()?.let { cached ->
                        Result.success(cached.toSystemState())
                    } ?: Result.failure(e)
                }
            } else {
                // Оффлайн-режим: загружаем последнее кэшированное состояние
                localDb.systemStateDao().getLatestState()?.let { cached ->
                    Result.success(cached.toSystemState())
                } ?: Result.failure(Exception("Нет подключения к сети и отсутствуют кэшированные данные."))
            }
        }
    }

    // Функция проверки наличия подключения к сети
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
