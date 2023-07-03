package com.example.flipstudy.ui.navigation

import android.media.Ringtone
import android.os.Build
import android.os.VibratorManager
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.flipstudy.ui.label.data.LabelDatabase
import com.example.flipstudy.ui.screens.SettingsScreen
import com.example.flipstudy.ui.screens.StatisticsScreen
import com.example.flipstudy.ui.screens.TimerScreen

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier,
    db: LabelDatabase,
    sensorAvailability: MutableState<Boolean>,
    sensorValues: MutableState<Float>,
    vibratorManager: VibratorManager,
    ringtone: Ringtone,
){
    NavHost(navController = navController, startDestination = Screens.Timer.route, modifier){
        composable(Screens.Settings.route) { SettingsScreen(navController) }
        composable(Screens.Timer.route) { TimerScreen(navController, db, sensorAvailability, sensorValues, vibratorManager, ringtone) }
        composable(Screens.Statistics.route) { StatisticsScreen(navController) }
    }
}
