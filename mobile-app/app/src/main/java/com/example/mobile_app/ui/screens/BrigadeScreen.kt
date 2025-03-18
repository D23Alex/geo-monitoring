// BrigadeScreen.kt
package com.example.mobile_app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mobile_app.model.Worker

@Composable
fun BrigadeScreen(navController: NavController) {
    val brigadeName = "Бригада А"
    // Пример списка рабочих
    val brigadeLeader = Worker("Иван Иванов", "Бригадир", "profile_url")
    val workers = listOf(
        brigadeLeader,
        Worker("Петр Петров", "Рабочий", "profile_url"),
        Worker("Сергей Сергеев", "Рабочий", "profile_url")
    )
    // Dummy роль пользователя
    val currentUserRole = "Бригадир"
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(text = brigadeName, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        if (currentUserRole == "Бригадир") {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(
                    onClick = { navController.navigate("assign_worker") },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Назначить объекты")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { navController.navigate("assign_task") },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Назначить задачи")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        // Дополнительная кнопка для перехода к списку объектов
        Button(
            onClick = { navController.navigate("objects_list") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Перейти к объектам")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Список рабочих:", style = MaterialTheme.typography.titleMedium)
        LazyColumn {
            items(workers) { worker ->
                WorkerItem(worker = worker, onClick = {
                    // Переход к профилю рабочего при нажатии
                    navController.navigate("worker_profile/${worker.name}")
                })
            }
        }
    }
}


@Composable
fun WorkerItem(worker: Worker, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = worker.name, style = MaterialTheme.typography.bodyLarge)
            Text(text = worker.role, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
