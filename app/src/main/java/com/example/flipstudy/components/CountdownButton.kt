package com.example.flipstudy.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun CountdownButton(action: TimerAction,
                    modifier: Modifier = Modifier,
                    onClick: () -> Unit) {

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if(action.text != "delete") {
            Text(
                text = action.text,
                fontSize = 36.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
        } else {
            action.content()
        }
    }
}