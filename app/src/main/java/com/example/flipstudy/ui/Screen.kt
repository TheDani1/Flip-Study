package com.example.flipstudy.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.flipstudy.R

sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object Settings : Screen("settings", R.string.settings, Icons.Filled.Settings)
    object Statistics : Screen("statistics", R.string.statistics, Icons.Filled.Info)
    object Timer : Screen("timer", R.string.timer, Icons.Filled.Star)
}
