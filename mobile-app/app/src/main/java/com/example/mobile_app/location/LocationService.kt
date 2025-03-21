package com.example.mobile_app.location

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import com.example.mobile_app.model.WorkerPositionUpdateEvent
import com.example.mobile_app.repository.EventRepository
import com.google.android.gms.location.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class LocationService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var repository: EventRepository
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onCreate() {
        super.onCreate()
        repository = EventRepository(applicationContext)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        startForegroundWithNotification() // стартуем сервис в режиме foreground

        // Инициализируем callback для получения обновлений местоположения
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    sendLocationEvent(location)
                }
            }
        }
        startLocationUpdates()
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
        serviceScope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    // Запуск уведомления и перевод сервиса в режим foreground
    private fun startForegroundWithNotification() {
        val channelId = "location_channel"
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Location Tracking",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Отслеживание местоположения")
            .setContentText("Сервис работает в фоне")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .build()

        startForeground(1, notification)
    }

    // Запускаем получение обновлений местоположения с использованием FusedLocationProviderClient
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 60000L)
            .setMinUpdateIntervalMillis(30000L)
            .build()
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    // Формирование и отправка события обновления местоположения
    private fun sendLocationEvent(location: Location) {
        val timestamp = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.format(Date())

        val event = WorkerPositionUpdateEvent(
            workerId = 1L, // Замените на реальный идентификатор пользователя
            latitude = location.latitude,
            longitude = location.longitude,
            timestamp = timestamp
        )

        serviceScope.launch {
            if (isNetworkAvailable()) {
                repository.sendEvent(event)
                repository.resendUnsentEvents()
                cancelOfflineNotification()
            } else {
                // При отсутствии сети событие сохраняется локально
                repository.sendEvent(event)
                showOfflineNotification()
            }
        }
    }

    // Проверка наличия подключения к сети
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    // Отображение уведомления об офлайн‑режиме
    private fun showOfflineNotification() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "offline_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Offline Mode", NotificationManager.IMPORTANCE_LOW)
            notificationManager.createNotificationChannel(channel)
        }
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Оффлайн режим")
            .setContentText("Нет соединения с сетью. Данные сохраняются и будут отправлены позже.")
            .setSmallIcon(android.R.drawable.stat_notify_error)
            .setOngoing(true)
            .build()
        startForeground(1, notification)
    }

    // При восстановлении сети обновляем foreground-уведомление основного канала
    private fun cancelOfflineNotification() {
        startForeground(2, NotificationCompat.Builder(this, "location_channel")
            .setContentTitle("Отслеживание местоположения")
            .setContentText("Сервис работает в фоне")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .build())
    }
}
