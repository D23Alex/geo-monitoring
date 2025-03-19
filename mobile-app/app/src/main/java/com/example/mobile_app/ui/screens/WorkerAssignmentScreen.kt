// WorkerAssignmentScreen.kt
package com.example.mobile_app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WorkerAssignmentScreen(workerName: String?, objectName: String?) {
    var assignedWorker by remember { mutableStateOf(workerName ?: "") }
    var assignedObject by remember { mutableStateOf(objectName ?: "") }
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = assignedWorker,
            onValueChange = { assignedWorker = it },
            label = { Text("Работник") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = assignedObject,
            onValueChange = { assignedObject = it },
            label = { Text("Объект") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            // Здесь API вызов для назначения работника на объект
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Назначить")
        }
    }
}
