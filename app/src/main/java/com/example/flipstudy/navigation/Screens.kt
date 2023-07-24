package com.example.flipstudy.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.flipstudy.R


/**
 * Clase de dato que contiene las rutas de las diferentes pantallas de la aplicación
 *
 */
sealed class Screens(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object Statistics : Screens("statistics", R.string.statistics, Icons.Filled.Info)
    object Timer : Screens("timer", R.string.timer, Icons.Filled.Star)
}
