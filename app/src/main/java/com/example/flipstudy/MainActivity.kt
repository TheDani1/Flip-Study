package com.example.flipstudy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.compose.FlipStudyTheme
import com.example.flipstudy.ui.navigation.AppNavigation
import com.example.flipstudy.ui.screens.MainScreen
import com.example.flipstudy.ui.screens.TimerScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlipStudyTheme {
                MainScreen()
            }
        }
    }
}