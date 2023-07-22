package com.example.flipstudy.screens

import android.media.Ringtone
import android.os.Build
import android.os.Vibrator
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.flipstudy.components.BottomNavigation
import com.example.flipstudy.label.data.LabelDatabase
import com.example.flipstudy.navigation.AppNavigation
import com.example.flipstudy.statistics.GoalsPreferences
import com.example.flipstudy.statistics.StatisticViewModel

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun MainScreen(
    db: LabelDatabase,
    sensorValues: Float,
    vibratorManager: Vibrator,
    ringtone: Ringtone,
    orientation: Int,
    goalsPreferences: GoalsPreferences,
    statisticViewModel: StatisticViewModel,
    rotationValues: Float
) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigation(navController = navController) }
    ) {innerPadding ->
        AppNavigation(navController, Modifier.padding(innerPadding), db, sensorValues, vibratorManager, ringtone, orientation, goalsPreferences, statisticViewModel, rotationValues)
    }
}