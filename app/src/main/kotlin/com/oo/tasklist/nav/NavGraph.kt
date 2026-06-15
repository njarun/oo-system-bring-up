package com.oo.tasklist.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.oo.tasklist.ui.TaskListScreen

internal const val ROUTE_TASKS = "tasks"

@Composable
fun AppNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = ROUTE_TASKS) {
        composable(ROUTE_TASKS) {
            TaskListScreen()
        }
    }
}
