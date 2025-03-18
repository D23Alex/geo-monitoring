// ObjectDetailsScreen.kt
package com.example.mobile_app.ui.screens

import com.example.mobile_app.model.SiteObject

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun ObjectDetailsScreen(objectId: String, navController: NavHostController) {
    // В реальном приложении данные берутся из ViewModel/API
    val dummyObject = SiteObject(objectId.toLongOrNull() ?: 0, "Объект $objectId", "Детальное описание объекта $objectId")
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(dummyObject.name, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(dummyObject.description, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = { navController.navigate("object_tasks/$objectId") }) {
                Text("Задачи")
            }
            Button(onClick = {
                // Переход на назначение работника на этот объект – без предзаполненного имени работника
                navController.navigate("assign_worker//${dummyObject.name}")
            }) {
                Text("Назначить работника")
            }
        }
    }
}
