// ObjectTasksScreen.kt
package com.example.mobile_app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Dummy-модель задачи
data class ObjectTask(val id: Long, val title: String, val status: String)

@Composable
fun ObjectTasksScreen(objectId: String) {
    // Dummy задачи для объекта
    val tasks = listOf(
        ObjectTask(1, "Задача 1 для объекта $objectId", "Выполнена"),
        ObjectTask(2, "Задача 2 для объекта $objectId", "В процессе"),
        ObjectTask(3, "Задача 3 для объекта $objectId", "Не начата")
    )
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn {
            items(tasks) { task ->
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(task.title, style = MaterialTheme.typography.titleMedium)
                        Text("Статус: ${task.status}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
