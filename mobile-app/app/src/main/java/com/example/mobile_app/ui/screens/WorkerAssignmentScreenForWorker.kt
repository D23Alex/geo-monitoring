package com.example.mobile_app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WorkerAssignmentScreenForWorker(workerId: String) {
    var taskIdInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = taskIdInput,
            onValueChange = { taskIdInput = it },
            label = { Text("Введите ID задачи") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                // Здесь необходимо вызвать API или обновить данные через ViewModel,
                // чтобы назначить работника (workerName) на задачу с ID taskIdInput.
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Назначить")
        }
    }
}