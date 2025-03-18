package com.example.mobile_app.worker

import android.app.Service
import android.content.Intent
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import com.example.mobile_app.model.LocationEvent
import com.example.mobile_app.network.APIService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LocationService : Service() {
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable
    private val interval: Long = 60000 // 1 минута

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
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun sendLocationEvent() {
        // Используем тестовое местоположение
        val location = Location("dummyprovider").apply {
            latitude = 55.7558
            longitude = 37.6173
            accuracy = 5.0f
        }
        val event = LocationEvent(location.latitude, location.longitude, location.accuracy)
        Thread {
            try {
                if (isNetworkAvailable()) {
                    val retrofit = Retrofit.Builder()
                        .baseUrl("https://your.api.server/") // Замените на URL вашего API сервера
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                    val apiService = retrofit.create(APIService::class.java)
                    val response = apiService.sendEvent(event).execute()
                    // Обработка ответа при необходимости
                } else {
                    // Если сеть отсутствует, сохранить событие для повторной отправки
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Сохранить событие для повторной отправки при восстановлении сети
            }
        }.start()
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
