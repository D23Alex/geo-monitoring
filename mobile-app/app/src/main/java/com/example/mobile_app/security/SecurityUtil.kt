package com.example.mobile_app.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.KeyGenerator
import javax.crypto.Mac
import javax.crypto.SecretKey

object SecurityUtil {
    private const val KEY_ALIAS = "HMAC_KEY"

    // Получение секретного ключа из AndroidKeyStore (генерируется, если отсутствует)
    private fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        return if (keyStore.containsAlias(KEY_ALIAS)) {
            keyStore.getKey(KEY_ALIAS, null) as SecretKey
        } else {
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_HMAC_SHA256, "AndroidKeyStore")
            val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY
            )
                // HMAC не требует блока шифрования и паддинга
                .setDigests(KeyProperties.DIGEST_SHA256)
                .build()
            keyGenerator.init(keyGenParameterSpec)
            keyGenerator.generateKey()
        }
    }

    // Вычисление HMAC для строки data
    fun computeHMAC(data: String): String {
        val secretKey = getSecretKey()
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(secretKey)
        val bytes = mac.doFinal(data.toByteArray(Charsets.UTF_8))
        return bytes.joinToString(separator = "") { String.format("%02x", it) }
    }

    // Проверка подписи
    fun verifyHMAC(data: String, signature: String): Boolean {
        val computedSignature = computeHMAC(data)
        return computedSignature == signature
    }
}
