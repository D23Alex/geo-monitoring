package com.example.mobile_app

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.content.ContextCompat
import com.example.mobile_app.location.LocationService
import com.example.mobile_app.ui.theme.MobileAppTheme
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.single.PermissionListener
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse

class MainActivity : ComponentActivity() {

    companion object {
        const val PREFS_NAME = "auth"
        const val KEY_USERNAME = "username"
        const val KEY_PASSWORD = "password"
        const val KEY_ROLE = "role"
        const val KEY_IS_LOGGED_IN = "isLoggedIn"
        const val TAG = "MainActivity"
    }

    // Разрешения, которые мы будем проверять/запрашивать
    private val networkStatePermission = Manifest.permission.ACCESS_NETWORK_STATE
    private val fineLocationPermission = Manifest.permission.ACCESS_FINE_LOCATION
    // FOREGROUND_SERVICE_LOCATION отсутствует в Manifest.permission, поэтому задаём вручную:
    private val foregroundServiceLocationPermission = "android.permission.FOREGROUND_SERVICE_LOCATION"
    private val backgroundLocationPermission = Manifest.permission.ACCESS_BACKGROUND_LOCATION
    private val foregroundServicePermission = Manifest.permission.FOREGROUND_SERVICE
    private val internetPermission = Manifest.permission.INTERNET

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate вызван")

        // Инициализация SharedPreferences с дефолтными значениями
        val prefs: SharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        if (!prefs.contains(KEY_USERNAME)) {
            prefs.edit().apply {
                putString(KEY_USERNAME, "login")
                putString(KEY_PASSWORD, "password")
                putString(KEY_ROLE, "Бригадир")
                putBoolean(KEY_IS_LOGGED_IN, false)
                apply()
            }
            Log.d(TAG, "SharedPreferences инициализированы с дефолтными значениями")
        } else {
            Log.d(TAG, "SharedPreferences уже содержат данные")
        }

        // Проверка и последовательный запрос всех нужных разрешений
        checkAndRequestPermissions()

        // Определяем стартовый экран
        val isLoggedIn = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
        val startDestination = if (isLoggedIn) "brigade" else "login"
        Log.d(TAG, "Стартовый экран: $startDestination")

        setContent {
            MobileAppTheme {
                MainApp(startDestination = startDestination)
            }
        }
    }

    // Последовательная проверка всех разрешений
    private fun checkAndRequestPermissions() {
        when {
            !hasNetworkStatePermission() -> {
                showPermissionRequestDialog(
                    title = "Требуется доступ к состоянию сети",
                    message = "Для работы приложения необходимо проверять состояние сети.",
                    onPositive = { requestNetworkStatePermission() },
                    onNegative = { showPermissionDeniedDialog() }
                )
            }
            !hasFineLocationPermission() -> {
                showPermissionRequestDialog(
                    title = "Требуется доступ к точному местоположению",
                    message = "Для работы приложения необходимо разрешить доступ к вашему точному местоположению.",
                    onPositive = { requestFineLocationPermission() },
                    onNegative = { showPermissionDeniedDialog() }
                )
            }
            !hasForegroundServiceLocationPermission() -> {
                showPermissionRequestDialog(
                    title = "Требуется разрешение для foreground-сервиса местоположения",
                    message = "Для работы приложения необходимо разрешить использование сервиса местоположения в режиме foreground.",
                    onPositive = { requestForegroundServiceLocationPermission() },
                    onNegative = { showPermissionDeniedDialog() }
                )
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !hasBackgroundLocationPermission() -> {
                showPermissionRequestDialog(
                    title = "Требуется фоновый доступ к местоположению",
                    message = "Чтобы приложение могло отслеживать ваше местоположение в фоне, необходимо предоставить разрешение \"Доступ в любое время\".",
                    onPositive = { requestBackgroundLocationPermission() },
                    onNegative = { showPermissionDeniedDialog() }
                )
            }
            !hasForegroundServicePermission() -> {
                showPermissionRequestDialog(
                    title = "Требуется разрешение на foreground-сервис",
                    message = "Для корректной работы приложения необходимо разрешить запуск foreground-сервиса.",
                    onPositive = { requestForegroundServicePermission() },
                    onNegative = { showPermissionDeniedDialog() }
                )
            }
            !hasInternetPermission() -> {
                showPermissionRequestDialog(
                    title = "Требуется доступ в Интернет",
                    message = "Для работы приложения необходимо разрешить доступ в Интернет.",
                    onPositive = { requestInternetPermission() },
                    onNegative = { showPermissionDeniedDialog() }
                )
            }
            else -> onAllLocationPermissionsGranted()
        }
    }

    // Проверка каждого разрешения
    private fun hasNetworkStatePermission(): Boolean =
        ContextCompat.checkSelfPermission(this, networkStatePermission) == PackageManager.PERMISSION_GRANTED

    private fun hasFineLocationPermission(): Boolean =
        ContextCompat.checkSelfPermission(this, fineLocationPermission) == PackageManager.PERMISSION_GRANTED

    private fun hasForegroundServiceLocationPermission(): Boolean =
        ContextCompat.checkSelfPermission(this, foregroundServiceLocationPermission) == PackageManager.PERMISSION_GRANTED

    private fun hasBackgroundLocationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContextCompat.checkSelfPermission(this, backgroundLocationPermission) == PackageManager.PERMISSION_GRANTED
        } else true
    }

    private fun hasForegroundServicePermission(): Boolean =
        ContextCompat.checkSelfPermission(this, foregroundServicePermission) == PackageManager.PERMISSION_GRANTED

    private fun hasInternetPermission(): Boolean =
        ContextCompat.checkSelfPermission(this, internetPermission) == PackageManager.PERMISSION_GRANTED

    // Универсальный диалог для запроса разрешения с кнопками "Разрешить" и "Отмена"
    private fun showPermissionRequestDialog(
        title: String,
        message: String,
        onPositive: () -> Unit,
        onNegative: () -> Unit
    ) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Разрешить") { dialog, _ ->
                dialog.dismiss()
                onPositive()
            }
            .setNegativeButton("Отмена") { dialog, _ ->
                dialog.dismiss()
                onNegative()
            }
            .setCancelable(false)
            .show()
    }

    // Запрос разрешения ACCESS_NETWORK_STATE
    private fun requestNetworkStatePermission() {
        Dexter.withContext(this)
            .withPermission(networkStatePermission)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    Log.d(TAG, "$networkStatePermission получено")
                    checkAndRequestPermissions()
                }
                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    Log.d(TAG, "$networkStatePermission не предоставлено")
                    showPermissionDeniedDialog()
                }
                override fun onPermissionRationaleShouldBeShown(
                    permission: com.karumi.dexter.listener.PermissionRequest,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    // Запрос разрешения ACCESS_FINE_LOCATION
    private fun requestFineLocationPermission() {
        Dexter.withContext(this)
            .withPermission(fineLocationPermission)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    Log.d(TAG, "$fineLocationPermission получено")
                    checkAndRequestPermissions()
                }
                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    Log.d(TAG, "$fineLocationPermission не предоставлено")
                    showPermissionDeniedDialog()
                }
                override fun onPermissionRationaleShouldBeShown(
                    permission: com.karumi.dexter.listener.PermissionRequest,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    // Запрос разрешения FOREGROUND_SERVICE_LOCATION
    private fun requestForegroundServiceLocationPermission() {
        Dexter.withContext(this)
            .withPermission(foregroundServiceLocationPermission)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    Log.d(TAG, "$foregroundServiceLocationPermission получено")
                    checkAndRequestPermissions()
                }
                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    Log.d(TAG, "$foregroundServiceLocationPermission не предоставлено")
                    showPermissionDeniedDialog()
                }
                override fun onPermissionRationaleShouldBeShown(
                    permission: com.karumi.dexter.listener.PermissionRequest,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    // Запрос разрешения ACCESS_BACKGROUND_LOCATION
    private fun requestBackgroundLocationPermission() {
        Dexter.withContext(this)
            .withPermission(backgroundLocationPermission)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    Log.d(TAG, "$backgroundLocationPermission получено")
                    checkAndRequestPermissions()
                }
                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    Log.d(TAG, "$backgroundLocationPermission не предоставлено")
                    showPermissionDeniedDialog()
                }
                override fun onPermissionRationaleShouldBeShown(
                    permission: com.karumi.dexter.listener.PermissionRequest,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    // Запрос разрешения FOREGROUND_SERVICE
    private fun requestForegroundServicePermission() {
        Dexter.withContext(this)
            .withPermission(foregroundServicePermission)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    Log.d(TAG, "$foregroundServicePermission получено")
                    checkAndRequestPermissions()
                }
                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    Log.d(TAG, "$foregroundServicePermission не предоставлено")
                    showPermissionDeniedDialog()
                }
                override fun onPermissionRationaleShouldBeShown(
                    permission: com.karumi.dexter.listener.PermissionRequest,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    // Запрос разрешения INTERNET
    private fun requestInternetPermission() {
        Dexter.withContext(this)
            .withPermission(internetPermission)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    Log.d(TAG, "$internetPermission получено")
                    checkAndRequestPermissions()
                }
                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    Log.d(TAG, "$internetPermission не предоставлено")
                    showPermissionDeniedDialog()
                }
                override fun onPermissionRationaleShouldBeShown(
                    permission: com.karumi.dexter.listener.PermissionRequest,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    // Если все разрешения получены, запускаем сервис локации
    private fun onAllLocationPermissionsGranted() {
        Log.d(TAG, "Все необходимые разрешения получены")
        startLocationService()
    }

    // Запуск сервиса локации
    private fun startLocationService() {
        Log.d(TAG, "Запуск LocationService")
        startService(Intent(this, LocationService::class.java))
    }

    // Диалог, информирующий о том, что разрешения не предоставлены
    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(this)
            .setTitle("Разрешения не предоставлены")
            .setMessage("Для работы приложения необходим доступ ко всем требуемым разрешениям. Приложение будет закрыто.")
            .setPositiveButton("Разрешить") { dialog, _ ->
                dialog.dismiss()
                checkAndRequestPermissions()
            }
            .setNegativeButton("Отмена") { dialog, _ ->
                dialog.dismiss()
                finish()
            }
            .setCancelable(false)
            .show()
    }
}
