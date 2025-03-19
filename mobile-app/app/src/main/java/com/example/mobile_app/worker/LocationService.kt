package com.example.mobile_app.worker

import android.app.Service
import android.content.Intent
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import com.example.mobile_app.model.WorkerPositionUpdateEvent
import com.example.mobile_app.network.ApiClient
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class LocationService : Service() {

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable
    private val interval: Long = 60000 // 1 минута
    // Создаем корутинный скоуп для фоновых операций
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        runnable = object : Runnable {
            override fun run() {
                sendLocationEvent()
                handler.postDelayed(this, interval)
            }
        }
        handler.post(runnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
        serviceScope.cancel() // отменяем все корутины при остановке сервиса
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun sendLocationEvent() {
        // Используем тестовое местоположение
        val location = Location("dummyprovider").apply {
            latitude = 55.7558
            longitude = 37.6173
            // Если необходимо, можно добавить обработку точности
        }
        // Предположим, что workerId берется из настроек или фиксирован
        val workerId = 1L

        // Форматирование текущего времени в ISO-8601 (UTC)
        val timestamp = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.format(Date())

        // Формируем событие обновления позиции работника
        val event = WorkerPositionUpdateEvent(
            workerId = workerId,
            latitude = location.latitude,
            longitude = location.longitude,
            timestamp = timestamp
        )

        // Отправка события асинхронно через корутину
        serviceScope.launch {
            try {
                if (isNetworkAvailable()) {
                    val response = ApiClient.apiService.sendEvent(event)
                    if (response.isSuccessful) {
                        // При необходимости: обработать успешный ответ (например, логирование)
                    } else {
                        // Если сервер вернул ошибку, можно сохранить событие для повторной отправки
                    }
                } else {
                    // Если сеть недоступна, сохранить событие локально для повторной отправки при восстановлении соединения
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Сохранить событие для повторной отправки при восстановлении сети
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
