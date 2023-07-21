package com.example.flipstudy.statistics.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.SportsScore
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flipstudy.statistics.DateUtils

@Composable
fun GoalRow(text : String, goal : MutableState<Int>){
    Text(text = text, style = MaterialTheme.typography.displaySmall)
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(17.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = { if(goal.value >= 600) goal.value -= 300 }, modifier = Modifier
            .size(42.dp)
            .clip(CircleShape), colors = IconButtonDefaults.filledIconButtonColors(MaterialTheme.colorScheme.primaryContainer)) {
            Icon(Icons.Default.Remove, contentDescription = "Menos", tint = MaterialTheme.colorScheme.onPrimaryContainer)
        }

        Text(text = DateUtils.fromSecondsToTime(goal.value), style = MaterialTheme.typography.titleLarge)

        IconButton(onClick = { if(goal.value <= 219660) goal.value += 300 }, modifier = Modifier
            .size(42.dp)
            .clip(CircleShape), colors = IconButtonDefaults.filledIconButtonColors(MaterialTheme.colorScheme.primaryContainer)) {
            Icon(Icons.Default.Add, contentDescription = "Mas", tint = MaterialTheme.colorScheme.onPrimaryContainer)
        }

    }
}