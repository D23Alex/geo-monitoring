// LocationService.kt (изменённый фрагмент)
package com.example.mobile_app.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.example.mobile_app.model.WorkerPositionUpdateEvent
import com.example.mobile_app.repository.EventRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class LocationService : Service() {

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable
    private val interval: Long = 60000 // 1 минута
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private lateinit var repository: EventRepository

    override fun onCreate() {
        super.onCreate()
        // Инициализируем репозиторий, передав контекст
        repository = EventRepository(applicationContext)

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
        serviceScope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun sendLocationEvent() {
        val location = Location("dummyprovider").apply {
            latitude = 55.7558
            longitude = 37.6173
        }
        val workerId = 1L
        val timestamp = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.format(Date())

        val event = WorkerPositionUpdateEvent(
            workerId = workerId,
            latitude = location.latitude,
            longitude = location.longitude,
            timestamp = timestamp
        )

        serviceScope.launch {
            if (isNetworkAvailable()) {
                // Пытаемся отправить событие и при этом очищаем локальные сохранённые
                repository.sendEvent(event)
                repository.resendUnsentEvents()
                cancelOfflineNotification()
            } else {
                // При отсутствии сети событие сохраняется локально внутри sendEvent
                repository.sendEvent(event)
                showOfflineNotification()
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    // Пример уведомления об оффлайн-режиме (для Android O+ создаём канал)
    private fun showOfflineNotification() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "offline_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Offline Mode", NotificationManager.IMPORTANCE_LOW)
            notificationManager.createNotificationChannel(channel)
        }
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Оффлайн режим")
            .setContentText("Нет соединения с сетью. События будут сохранены и отправлены позже.")
            .setSmallIcon(android.R.drawable.stat_notify_error)
            .setOngoing(true)
            .build()
        startForeground(1, notification)
    }

    // Убираем уведомление при восстановлении сети
    private fun cancelOfflineNotification() {
        stopForeground(STOP_FOREGROUND_REMOVE)

    }
}
