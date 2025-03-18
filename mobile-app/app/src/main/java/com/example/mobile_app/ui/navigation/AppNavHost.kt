// AppNavHost.kt
package com.example.mobile_app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mobile_app.ui.screens.BrigadeScreen
import com.example.mobile_app.ui.screens.ObjectsScreen
import com.example.mobile_app.ui.screens.MessagesScreen
import com.example.mobile_app.ui.screens.RegistrationScreen
import com.example.mobile_app.ui.screens.LoginScreen
import com.example.mobile_app.ui.screens.WorkerProfileScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier
) {
    NavHost(navController = navController, startDestination = startDestination, modifier = modifier) {
        composable("brigade") { BrigadeScreen() }
        composable("objects") { ObjectsScreen() }
        composable("messages") { MessagesScreen() }
        composable("login") {
            LoginScreen(onLoginSuccess = {
                navController.navigate("brigade") {
                    popUpTo("login") { inclusive = true }
                }
            }, onNavigateToRegistration = {
                navController.navigate("registration")
            })
        }
        composable("registration") {
            RegistrationScreen(onRegistrationSuccess = {
                navController.navigate("brigade") {
                    popUpTo("registration") { inclusive = true }
                }
            })
        }
        composable("worker_profile") { WorkerProfileScreen() }
    }
}
