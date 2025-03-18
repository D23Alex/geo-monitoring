// AppNavHost.kt
package com.example.mobile_app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mobile_app.ui.screens.*


@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier
) {
    NavHost(navController = navController, startDestination = startDestination, modifier = modifier) {
        composable("brigade") {
            BrigadeScreen(navController = navController)
        }
        composable("objects_list") {
            ObjectsListScreen(navController = navController)
        }
        composable("messages") { MessagesScreen() }
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
        // Профиль рабочего
        composable("worker_profile/{workerName}") { backStackEntry ->
            val workerName = backStackEntry.arguments?.getString("workerName")
            WorkerProfileScreen(navController = navController, workerName = workerName)
        }
        // Экран назначения работника на объект – с параметрами
        composable("assign_worker/{workerName}/{objectName}") { backStackEntry ->
            val workerName = backStackEntry.arguments?.getString("workerName")
            val objectName = backStackEntry.arguments?.getString("objectName")
            WorkerAssignmentScreen(workerName = workerName, objectName = objectName)
        }
        // Экран назначения работника – без предзаполнения
        composable("assign_worker") {
            WorkerAssignmentScreen(workerName = null, objectName = null)
        }
        // Страница объекта
        composable("object_details/{objectId}") { backStackEntry ->
            val objectId = backStackEntry.arguments?.getString("objectId") ?: "0"
            ObjectDetailsScreen(objectId = objectId, navController = navController)
        }
        // Страница задач для объекта
        composable("object_tasks/{objectId}") { backStackEntry ->
            val objectId = backStackEntry.arguments?.getString("objectId") ?: "0"
            ObjectTasksScreen(objectId = objectId)
        }
        // Новый маршрут для назначения задач на объект
        composable("assign_task") {
            TaskAssignmentScreen()
        }
    }
}
