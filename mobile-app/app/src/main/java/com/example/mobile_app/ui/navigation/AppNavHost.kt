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
import com.example.mobile_app.ui.screens.TaskDetailsScreen
import com.example.mobile_app.ui.screens.WorkerAssignmentScreenForTask
import com.example.mobile_app.ui.screens.WorkerAssignmentScreenForWorker
import com.example.mobile_app.ui.screens.WorkerProfileScreen


@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    onDrawerClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(navController = navController, startDestination = startDestination, modifier = modifier) {
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
        composable("task_details/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId") ?: "0"
            ScreenWithAppBar(
                title = "Детали задачи",
                showBackButton = true,
                navController = navController,
                onDrawerClicked = onDrawerClicked
            ) {
                TaskDetailsScreen(taskId = taskId, navController = navController)
            }
        }
        composable("assign_worker_to_task/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId") ?: "0"
            ScreenWithAppBar(
                title = "Назначение работника на задачу",
                showBackButton = true,
                navController = navController,
                onDrawerClicked = onDrawerClicked
            ) {
                // Этот экран будет использоваться для назначения работника из TaskDetailsScreen
                WorkerAssignmentScreenForTask(taskId = taskId)
            }
        }
        composable("assign_worker_to_task_by_worker/{workerId}") { backStackEntry ->
            val workerId = backStackEntry.arguments?.getString("workerId") ?: "0"
            ScreenWithAppBar(
                title = "Назначение задачи для рабочего",
                showBackButton = true,
                navController = navController,
                onDrawerClicked = onDrawerClicked
            ) {
                // Этот экран используется для назначения задачи, инициированного из профиля рабочего
                WorkerAssignmentScreenForWorker(workerId = workerId)
            }
        }
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