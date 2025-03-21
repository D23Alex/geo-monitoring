package com.example.mobile_app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mobile_app.debug.DebugData
import com.example.mobile_app.model.TaskDetails

@Composable
fun TaskDetailsScreen(taskId: String, navController: NavHostController? = null) {
    // Для отладки используем dummy данные из DebugData
    val taskDetails: TaskDetails = DebugData.dummyTaskDetails

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Задача №${taskDetails.id}", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Описание: ${taskDetails.description}", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Статус: ${taskDetails.status}", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Создана: ${taskDetails.createdAt}", style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Активный интервал: ${taskDetails.activeInterval.from} - ${taskDetails.activeInterval.to}",
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Критерии завершения:", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn {
            items(taskDetails.completionCriteria.toList()) { (number, criterion) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Критерий $number: ${criterion.name}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Описание: ${criterion.description}", style = MaterialTheme.typography.bodySmall)
                        Text(text = "Комментарий: ${criterion.comment ?: "нет"}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Кнопка для назначения работника на задачу
        Button(onClick = {
            navController?.navigate("assign_worker_to_task/${taskDetails.id}")
        }) {
            Text("Назначить работника на задачу")
        }
    }
}
