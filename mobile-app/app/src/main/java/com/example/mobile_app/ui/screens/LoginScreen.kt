package com.example.mobile_app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.mobile_app.repository.ClientDataRepository

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegistration: () -> Unit
) {
    val context = LocalContext.current
    // Используем безопасное хранилище для данных клиента
    val clientRepo = ClientDataRepository(context)
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Вход", style = MaterialTheme.typography.headlineMedium)
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
        Button(onClick = {
            val storedData = clientRepo.getClientData()
            if (storedData != null && username == storedData.login && password == storedData.password) {
                onLoginSuccess()
            } else {
                errorMsg = "Неверные учетные данные"
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Войти")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Зарегистрироваться",
            modifier = Modifier.clickable { onNavigateToRegistration() },
            color = MaterialTheme.colorScheme.primary
        )
        if (errorMsg.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = errorMsg, color = MaterialTheme.colorScheme.error)
        }
    }
}
