// MainActivity.kt
package com.example.mobile_app

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.mobile_app.ui.navigation.AppNavHost
import com.example.mobile_app.ui.theme.MobileAppTheme
import com.example.mobile_app.ui.DrawerContent
import com.example.mobile_app.worker.LocationService
import kotlinx.coroutines.launch
import androidx.core.content.edit
import androidx.navigation.compose.currentBackStackEntryAsState

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    companion object {
        const val PREFS_NAME = "auth"
        const val KEY_USERNAME = "username"
        const val KEY_PASSWORD = "password"
        const val KEY_ROLE = "role"
        const val KEY_IS_LOGGED_IN = "isLoggedIn"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Запуск фонового сервиса для отправки данных местоположения
        startService(Intent(this, LocationService::class.java))
        // Инициализируем SharedPreferences с дефолтными данными, если их ещё нет
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        if (!prefs.contains(KEY_USERNAME)) {
            prefs.edit().apply {
                putString(KEY_USERNAME, "login")
                putString(KEY_PASSWORD, "password")
                putString(KEY_ROLE, "Бригадир") // По умолчанию роль бригадира
                putBoolean(KEY_IS_LOGGED_IN, false)
                apply()
            }
        }

        // Определяем стартовый экран в зависимости от флага авторизации
        val isLoggedIn = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
        val startDestination = if (isLoggedIn) "brigade" else "login"

        setContent {
            MobileAppTheme {
                MainApp(startDestination = startDestination)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(startDestination: String) {
    val navController = rememberNavController()
    // Получаем текущий маршрут
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    // Список маршрутов, относящихся к авторизации
    val authRoutes = listOf("login", "registration")
    // Если текущего маршрута ещё нет или он относится к авторизации,
    // отображаем простой Scaffold (без Drawer и TopAppBar)
    if (currentBackStackEntry?.destination?.route == null ||
        currentBackStackEntry!!.destination.route in authRoutes
    ) {
        Scaffold { innerPadding ->
            AppNavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.padding(innerPadding)
            )
        }
    } else {
        // Если пользователь уже авторизован – показываем основной интерфейс с боковым меню
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val context = LocalContext.current
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                DrawerContent(
                    onDestinationClicked = { route ->
                        scope.launch { drawerState.close() }
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onExitClicked = {
                        // Сброс авторизации и переход на экран входа
                        val prefs: SharedPreferences =
                            context.getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE)
                        prefs.edit { putBoolean(MainActivity.KEY_IS_LOGGED_IN, false) }
                        scope.launch { drawerState.close() }
                        navController.navigate("login") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    }
                )
            }
        ) {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = { Text("Mobile App") },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Меню")
                            }
                        }
                    )
                }
            ) { innerPadding ->
                AppNavHost(
                    navController = navController,
                    startDestination = startDestination,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}
