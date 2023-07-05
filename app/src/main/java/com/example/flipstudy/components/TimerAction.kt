package com.example.flipstudy.components

import androidx.compose.runtime.Composable

data class TimerAction(
    val text: String,
    val content: @Composable () -> Unit = {}
)
