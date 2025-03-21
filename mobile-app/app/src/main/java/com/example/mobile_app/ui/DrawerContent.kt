// DrawerContent.kt
package com.example.mobile_app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DrawerContent(
    onDestinationClicked: (String) -> Unit,
    onExitClicked: () -> Unit
) {
    // Оформляем окно как карточку с фиксированной шириной, что позволяет ему быть всплывающим и не занимать весь экран
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxHeight()
            .width(300.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface) // Непрозрачный фон
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Меню",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )
            HorizontalDivider()
            DrawerItem(label = "Бригада", route = "brigade", onDestinationClicked = onDestinationClicked)
            DrawerItem(label = "Объекты", route = "objects_list", onDestinationClicked = onDestinationClicked)
            DrawerItem(label = "Сообщения", route = "messages", onDestinationClicked = onDestinationClicked)
            Spacer(modifier = Modifier.weight(1f))
            // Кнопка "Выход"
            DrawerItem(label = "Выход", route = "logout", onDestinationClicked = { onExitClicked() })
        }
    }
}

@Composable
fun DrawerItem(label: String, route: String, onDestinationClicked: (String) -> Unit) {
    Text(
        text = label,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onDestinationClicked(route) }
            .padding(16.dp)
    )
}
