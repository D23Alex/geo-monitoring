// WorkerProfileScreen.kt
package com.example.mobile_app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun WorkerProfileScreen(navController: NavHostController, workerName: String?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Имя: ${workerName ?: "Неизвестно"}", style = MaterialTheme.typography.bodyLarge)
        // Дополнительная информация о рабочем может располагаться здесь
        Spacer(modifier = Modifier.height(16.dp))
        // Новая кнопка для назначения работника на задачу
        Button(onClick = {
            navController.navigate("assign_worker_to_task_by_worker/${workerName ?: ""}")
        }) {
            Text("Назначить работника на задачу")
        }
    }
}
