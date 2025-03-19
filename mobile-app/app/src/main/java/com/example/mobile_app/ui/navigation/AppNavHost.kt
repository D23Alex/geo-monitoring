// AppNavHost.kt
package com.example.mobile_app.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mobile_app.ui.screens.BrigadeScreen
import com.example.mobile_app.ui.screens.LoginScreen
import com.example.mobile_app.ui.screens.MessagesScreen
import com.example.mobile_app.ui.screens.ObjectDetailsScreen
import com.example.mobile_app.ui.screens.ObjectTasksScreen
import com.example.mobile_app.ui.screens.ObjectsListScreen
import com.example.mobile_app.ui.screens.RegistrationScreen
import com.example.mobile_app.ui.screens.TaskAssignmentScreen
import com.example.mobile_app.ui.screens.WorkerAssignmentScreen
import com.example.mobile_app.ui.screens.WorkerProfileScreen


@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    onDrawerClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(navController = navController, startDestination = startDestination, modifier = modifier) {
        // Главный экран "brigade" – без кнопки "Назад", с меню
        composable("brigade") {
            ScreenWithAppBar(
                title = "Бригада",
                showBackButton = false,
                navController = navController,
                onDrawerClicked = onDrawerClicked
            ) {
                BrigadeScreen(navController = navController)
            }
        }
        // Экран объектов
        composable("objects_list") {
            ScreenWithAppBar(
                title = "Список объектов",
                showBackButton = true,
                navController = navController,
                onDrawerClicked = onDrawerClicked
            ) {
                ObjectsListScreen(navController = navController)
            }
        }
        // Экран сообщений
        composable("messages") {
            ScreenWithAppBar(
                title = "Сообщения",
                showBackButton = true,
                navController = navController,
                onDrawerClicked = onDrawerClicked
            ) {
                MessagesScreen()
            }
        }
        // Экран входа – можно оставить без верхней шапки, если нужно
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("brigade") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegistration = {
                    navController.navigate("registration")
                }
            )
        }
        // Экран регистрации
        composable("registration") {
            ScreenWithAppBar(
                title = "Регистрация",
                showBackButton = true,
                navController = navController,
                onDrawerClicked = onDrawerClicked
            ) {
                RegistrationScreen(
                    onRegistrationSuccess = {
                        navController.navigate("brigade") {
                            popUpTo("registration") { inclusive = true }
                        }
                    },
                    onNavigateToLogin = {
                        navController.navigate("login") {
                            popUpTo("registration") { inclusive = true }
                        }
                    }
                )
            }
        }
        // Профиль рабочего
        composable("worker_profile/{workerName}") { backStackEntry ->
            val workerName = backStackEntry.arguments?.getString("workerName")
            ScreenWithAppBar(
                title = "Профиль рабочего",
                showBackButton = true,
                navController = navController,
                onDrawerClicked = onDrawerClicked
            ) {
                WorkerProfileScreen(navController = navController, workerName = workerName)
            }
        }
        // Экран назначения работника с параметрами
        composable("assign_worker/{workerName}/{objectName}") { backStackEntry ->
            val workerName = backStackEntry.arguments?.getString("workerName")
            val objectName = backStackEntry.arguments?.getString("objectName")
            ScreenWithAppBar(
                title = "Назначение работника",
                showBackButton = true,
                navController = navController,
                onDrawerClicked = onDrawerClicked
            ) {
                WorkerAssignmentScreen(workerName = workerName, objectName = objectName)
            }
        }
        // Экран назначения работника без параметров
        composable("assign_worker") {
            ScreenWithAppBar(
                title = "Назначение работника",
                showBackButton = true,
                navController = navController,
                onDrawerClicked = onDrawerClicked
            ) {
                WorkerAssignmentScreen(workerName = null, objectName = null)
            }
        }
        // Страница объекта
        composable("object_details/{objectId}") { backStackEntry ->
            val objectId = backStackEntry.arguments?.getString("objectId") ?: "0"
            ScreenWithAppBar(
                title = "Детали объекта",
                showBackButton = true,
                navController = navController,
                onDrawerClicked = onDrawerClicked
            ) {
                ObjectDetailsScreen(objectId = objectId, navController = navController)
            }
        }
        // Страница задач для объекта
        composable("object_tasks/{objectId}") { backStackEntry ->
            val objectId = backStackEntry.arguments?.getString("objectId") ?: "0"
            ScreenWithAppBar(
                title = "Задачи объекта",
                showBackButton = true,
                navController = navController,
                onDrawerClicked = onDrawerClicked
            ) {
                ObjectTasksScreen(objectId = objectId)
            }
        }
        // Экран назначения задачи
        composable("assign_task") {
            ScreenWithAppBar(
                title = "Назначение задачи",
                showBackButton = true,
                navController = navController,
                onDrawerClicked = onDrawerClicked
            ) {
                TaskAssignmentScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenWithAppBar(
    title: String,
    showBackButton: Boolean,
    navController: NavHostController,
    onDrawerClicked: () -> Unit,
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    if (showBackButton) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onDrawerClicked) {
                        Icon(Icons.Default.Menu, contentDescription = "Меню")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            content()
        }
    }
}