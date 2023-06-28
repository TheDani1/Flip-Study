package com.example.flipstudy.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.flipstudy.R



sealed class Screens(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object Settings : Screens("settings", R.string.settings, Icons.Filled.Settings)
    object Statistics : Screens("statistics", R.string.statistics, Icons.Filled.Info)
    object Timer : Screens("timer", R.string.timer, Icons.Filled.Star)
}
