package com.example.flipstudy.ui.screens

import android.media.Ringtone
import android.os.Build
import android.os.VibratorManager
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.flipstudy.ui.components.BottomNavigation
import com.example.flipstudy.ui.label.data.LabelDatabase
import com.example.flipstudy.ui.navigation.AppNavigation

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun MainScreen(
    db: LabelDatabase,
    sensorAvailability: MutableState<Boolean>,
    sensorValues: MutableState<Float>,
    vibratorManager: VibratorManager,
    ringtone: Ringtone,
) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigation(navController = navController) }
    ) {innerPadding ->
        AppNavigation(navController, Modifier.padding(innerPadding), db, sensorAvailability, sensorValues, vibratorManager, ringtone)
    }
}