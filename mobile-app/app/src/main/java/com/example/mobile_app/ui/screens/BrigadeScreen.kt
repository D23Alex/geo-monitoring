// BrigadeScreen.kt
package com.example.mobile_app.ui.screens

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mobile_app.model.Worker

@Composable
fun BrigadeScreen() {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    // Читаем роль из настроек (по умолчанию "Бригадир")
    val currentUserRole = prefs.getString("role", "Бригадир") ?: "Бригадир"

    val brigadeName = "Бригада А"
    val brigadeLeader = Worker(name = "Иван Иванов", role = "Бригадир", profileUrl = "profile_url")
    val workers = listOf(
        brigadeLeader,
        Worker(name = "Петр Петров", role = "Рабочий", profileUrl = "profile_url"),
        Worker(name = "Сергей Сергеев", role = "Рабочий", profileUrl = "profile_url")
    )

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(text = brigadeName, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        if (currentUserRole == "Бригадир") {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = { /* API: назначение на объекты */ }) {
                    Text("Назначить объекты")
                }
                Button(onClick = { /* API: назначение задач */ }) {
                    Text("Назначить задачи")
                }
//                Button(onClick = { /* API: контроль деятельности */ }) {
//                    Text("Контроль")
//                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        Text(text = "Список рабочих:", style = MaterialTheme.typography.titleMedium)
        LazyColumn {
            items(workers) { worker ->
                WorkerItem(worker = worker, onClick = {
                    // Переход к экрану профиля рабочего
                    // Здесь можно добавить навигацию, например, через navController
                })
            }
        }
    }
}

@Composable
fun WorkerItem(worker: Worker, onClick: () -> Unit) {
    Card(modifier = Modifier
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
