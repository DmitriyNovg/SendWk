package com.sendwk.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sendwk.app.presentation.auth.LoginScreen
import com.sendwk.app.presentation.chat.ChatScreen
import com.sendwk.app.presentation.profile.ProfileScreen
import com.sendwk.app.presentation.tasks.TaskScreen
import com.sendwk.app.presentation.settings.SettingsScreen
import com.sendwk.app.presentation.calendar.CalendarScreen
import com.sendwk.app.presentation.calls.CallsScreen  // <-- Добавь этот импорт

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Tasks : Screen("tasks")
    object Chat : Screen("chat")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
    object Calendar : Screen("calendar")
    object Calls : Screen("calls")  // <-- Добавь эту строку
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Tasks.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Tasks.route) {
            TaskScreen(
                onNavigateToChat = { navController.navigate(Screen.Chat.route) },
                onNavigateToProfile = { navController.navigate(Screen.Profile.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                onNavigateToCalendar = { navController.navigate(Screen.Calendar.route) },
                onNavigateToCalls = { navController.navigate(Screen.Calls.route) },  // <-- Добавлено
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Tasks.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Chat.route) {
            ChatScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onNavigateBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Profile.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.Calendar.route) {
            CalendarScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.Calls.route) {  // <-- Добавь этот блок
            CallsScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}