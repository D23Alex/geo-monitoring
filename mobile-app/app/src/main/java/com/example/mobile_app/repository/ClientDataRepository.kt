package com.example.mobile_app.repository

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

data class ClientData(
    val clientId: Long,
    val login: String,
    val password: String,
    val mac: String,
    val imei: String
)

class ClientDataRepository(context: Context) {
    // Создаем мастер-ключ с использованием актуального API (MasterKey.Builder)
    private val masterKey: MasterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    // Создаем EncryptedSharedPreferences с использованием позиционных параметров
    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "client_data_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
        private const val KEY_CLIENT_ID = "client_id"
        private const val KEY_LOGIN = "login"
        private const val KEY_PASSWORD = "password"
        private const val KEY_MAC = "mac"
        private const val KEY_IMEI = "imei"
    }

    fun saveClientData(clientId: Long, login: String, password: String, mac: String, imei: String) {
        sharedPreferences.edit().apply {
            putLong(KEY_CLIENT_ID, clientId)
            putString(KEY_LOGIN, login)
            putString(KEY_PASSWORD, password)
            putString(KEY_MAC, mac)
            putString(KEY_IMEI, imei)
            apply()
        }
    }

    fun getClientData(): ClientData? {
        if (!sharedPreferences.contains(KEY_CLIENT_ID)) return null
        return ClientData(
            clientId = sharedPreferences.getLong(KEY_CLIENT_ID, 0L),
            login = sharedPreferences.getString(KEY_LOGIN, "") ?: "",
            password = sharedPreferences.getString(KEY_PASSWORD, "") ?: "",
            mac = sharedPreferences.getString(KEY_MAC, "") ?: "",
            imei = sharedPreferences.getString(KEY_IMEI, "") ?: ""
        )
    }
}
