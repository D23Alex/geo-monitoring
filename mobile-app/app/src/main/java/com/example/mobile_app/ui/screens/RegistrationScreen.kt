// RegistrationScreen.kt
package com.example.mobile_app.ui.screens

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun RegistrationScreen(
    onRegistrationSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit // новый параметр для перехода на экран входа
) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text("Регистрация", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Имя пользователя") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (username.isNotBlank() && password.isNotBlank()) {
                    prefs.edit().apply {
                        putString("username", username)
                        putString("password", password)
                        putBoolean("isLoggedIn", true)
                        // Можно также сохранить роль, например "Бригадир"
                        putString("role", "Бригадир")
                        apply()
                    }
                    onRegistrationSuccess()
                } else {
                    errorMsg = "Введите корректные данные"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Зарегистрироваться")
        }
        if (errorMsg.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = errorMsg, color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(8.dp))
        // Добавляем ссылку для перехода на экран входа
        Text(
            text = "Уже есть аккаунт? Войти",
            modifier = Modifier.clickable { onNavigateToLogin() },
            color = MaterialTheme.colorScheme.primary
        )
    }
}
