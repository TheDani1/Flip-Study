package com.example.flipstudy.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flipstudy.ui.screens.SettingsScreen
import com.example.flipstudy.ui.screens.StatisticsScreen
import com.example.flipstudy.ui.screens.TimerScreen

@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier){
    NavHost(navController = navController, startDestination = Screens.Timer.route, modifier){
        composable(Screens.Settings.route) { SettingsScreen(navController) }
        composable(Screens.Timer.route) { TimerScreen(navController) }
        composable(Screens.Statistics.route) { StatisticsScreen(navController) }
    }
}
