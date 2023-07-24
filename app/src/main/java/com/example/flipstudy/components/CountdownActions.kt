package com.example.flipstudy.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme

/**
 * Variable que contiene cada una de las acciones de los botones del CountDown
 *
 *
 */
val CountdownActions = listOf(
    TimerAction(
        text = "1",
    ),
    TimerAction(
        text = "2",
    ),
    TimerAction(
        text = "3",
    ),
    TimerAction(
        text = "4",
    ),
    TimerAction(
        text = "5",
    ),
    TimerAction(
        text = "6",
    ),
    TimerAction(
        text = "7",
    ),
    TimerAction(
        text = "8",
    ),
    TimerAction(
        text = "9",
    ),
    TimerAction(
        text = "0",
    ),
    TimerAction(
        text = "delete",
        content = {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
    ),
)