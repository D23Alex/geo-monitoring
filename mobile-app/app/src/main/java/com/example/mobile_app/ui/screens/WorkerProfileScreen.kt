// WorkerProfileScreen.kt
package com.example.mobile_app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun WorkerProfileScreen(navController: NavController, workerName: String?) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text("Профиль рабочего", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Имя: ${workerName ?: "Неизвестно"}", style = MaterialTheme.typography.bodyLarge)
        // Дополнительная информация о рабочем
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            // Переход к окну назначения работника на объект с передачей workerName
            navController.navigate("assign_worker/${workerName ?: ""}/")
        }) {
            Text("Назначить на объект")
        }
    }
}
