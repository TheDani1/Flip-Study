package com.example.flipstudy.navigation

import android.media.Ringtone
import android.os.Build
import android.os.Vibrator
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.flipstudy.label.data.LabelDatabase
import com.example.flipstudy.screens.StatisticsScreen
import com.example.flipstudy.screens.TimerScreen

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier,
    db: LabelDatabase,
    sensorValues: MutableState<Float>,
    vibratorManager: Vibrator,
    ringtone: Ringtone,
    orientation: Int,
){
    NavHost(navController = navController, startDestination = Screens.Timer.route, modifier){
        composable(Screens.Timer.route) { TimerScreen(db, sensorValues, vibratorManager, ringtone, orientation) }
        composable(Screens.Statistics.route) { StatisticsScreen(db, orientation) }
    }
}
