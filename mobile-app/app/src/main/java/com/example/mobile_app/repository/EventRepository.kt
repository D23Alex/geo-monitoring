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
import com.example.mobile_app.security.SecurityUtil
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

    // Отправка события с сохранением локально с вычисленной подписью
    suspend fun sendEvent(event: Event): Result<EventResponse> {
        val eventJson = gson.toJson(event)
        val signature = SecurityUtil.computeHMAC(eventJson)
        val unsentEvent = UnsentEvent(
            eventJson = eventJson,
            timestamp = event.timestamp,
            eventType = event.eventType,
            signature = signature
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
                    Result.failure(e)
                }
            } else {
                Result.failure(Exception("Сеть недоступна. Событие сохранено локально и будет отправлено при восстановлении соединения."))
            }
        }
    }

    // Пытаемся отправить все сохранённые неотправленные события с проверкой целостности
    suspend fun resendUnsentEvents() {
        if (!isNetworkAvailable()) return

        val pendingEvents = eventDao.getAllEvents()
        for (unsent in pendingEvents) {
            // Проверяем подпись перед отправкой
            if (!SecurityUtil.verifyHMAC(unsent.eventJson, unsent.signature)) {
                // Если проверка не пройдена, логируем инцидент и пропускаем событие
                // Можно добавить дополнительную обработку (например, уведомление о проблеме)
                eventDao.deleteEvent(unsent)
                continue
            }
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

    // Удаляем локальное событие по совпадению по timestamp и eventType
    private suspend fun deleteLocalEvent(event: Event) {
        val pendingEvents = eventDao.getAllEvents().filter {
            it.eventType == event.eventType && it.timestamp == event.timestamp
        }
        pendingEvents.forEach { eventDao.deleteEvent(it) }
    }

    // Периодическая очистка старых событий (например, старше 1 дня)
    suspend fun createSnapshotAndCleanOldEvents() {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val cutoff = sdf.format(Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000))
        eventDao.deleteEventsOlderThan(cutoff)
        // Дополнительно можно сохранять текущее состояние в отдельной таблице
    }

    // Получение текущего состояния системы с использованием оффлайн-кеша
    suspend fun getCurrentState(): Result<SystemState> {
        return withContext(Dispatchers.IO) {
            if (isNetworkAvailable()) {
                try {
                    val response = apiService.getCurrentState()
                    if (response.isSuccessful) {
                        val state = response.body()!!
                        localDb.systemStateDao().insertState(state.toCachedState())
                        Result.success(state)
                    } else {
                        localDb.systemStateDao().getLatestState()?.let { cached ->
                            Result.success(cached.toSystemState())
                        } ?: Result.failure(Exception("Ошибка получения состояния: ${response.code()}"))
                    }
                } catch (e: Exception) {
                    localDb.systemStateDao().getLatestState()?.let { cached ->
                        Result.success(cached.toSystemState())
                    } ?: Result.failure(e)
                }
            } else {
                localDb.systemStateDao().getLatestState()?.let { cached ->
                    Result.success(cached.toSystemState())
                } ?: Result.failure(Exception("Нет подключения к сети и отсутствуют кэшированные данные."))
            }
        }
    }

    // Проверка наличия подключения к сети
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
