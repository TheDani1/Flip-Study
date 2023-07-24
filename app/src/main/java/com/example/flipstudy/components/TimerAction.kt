package com.example.flipstudy.components

import androidx.compose.runtime.Composable

/**
 * Clase de dato que determina una acción
 *
 * @property text Texto de la acción
 * @property content Contenido de la acción
 *
 */
data class TimerAction(
    val text: String,
    val content: @Composable () -> Unit = {}
)
