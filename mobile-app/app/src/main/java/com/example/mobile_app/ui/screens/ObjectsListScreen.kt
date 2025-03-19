// ObjectsListScreen.kt
package com.example.mobile_app.ui.screens

import com.example.mobile_app.model.SiteObject
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun ObjectsListScreen(navController: NavHostController) {
    // Dummy данные с примерами объектов и их задач
    val objects = listOf(
        SiteObject(1, "Объект A", "Описание объекта A"),
        SiteObject(2, "Объект B", "Описание объекта B"),
        SiteObject(3, "Объект C", "Описание объекта C")
    )
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn {
            items(objects) { obj ->
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { navController.navigate("object_details/${obj.id}") }) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(obj.name, style = MaterialTheme.typography.titleMedium)
                        Text(obj.description, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
