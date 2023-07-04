package com.example.flipstudy.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.example.flipstudy.ui.screens.horas
import com.example.flipstudy.ui.screens.minutos
import com.example.flipstudy.ui.screens.segundos

@Composable
fun CountdownSetterDialog(
    openDialog: MutableState<Boolean>,
    actions: List<TimerAction>,
    modifier: Modifier = Modifier,
    countdown: MutableState<String>,
    horas: MutableState<Int>,
    minutos: MutableState<Int>,
    segundos: MutableState<Int>
) {

    if (openDialog.value) {

        val countdownCopy = remember { mutableStateOf(if (countdown.value.toInt() == 0) "" else countdown.value.toInt().toString()) }

        val haptic = LocalHapticFeedback.current

        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                openDialog.value = false
            },
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier.fillMaxWidth(),
            icon = { Icon(Icons.Filled.HourglassEmpty, contentDescription = null) },
            title = {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    item {
                        Text(
                            text = horas(countdownCopy),
                            style = MaterialTheme.typography.displayLarge,
                            color = if(countdownCopy.value.length >= 5 ) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "h",
                            style = MaterialTheme.typography.titleLarge,
                            color = if(countdownCopy.value.length >= 5 ) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = minutos(countdownCopy),
                            style = MaterialTheme.typography.displayLarge,
                            color = if(countdownCopy.value.length >= 3 ) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "m",
                            style = MaterialTheme.typography.titleLarge,
                            color = if(countdownCopy.value.length >= 3 ) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = segundos(countdownCopy),
                            style = MaterialTheme.typography.displayLarge,
                            color = if(countdownCopy.value.isNotEmpty()) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "s",
                            style = MaterialTheme.typography.titleLarge,
                            color = if(countdownCopy.value.isNotEmpty()) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            text = {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    userScrollEnabled = false,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = modifier,
                ) {
                    items(actions) { action ->
                        CountdownButton(
                            action = action,
                            modifier = Modifier.aspectRatio(1f),
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                if(!(action.text == "0" && countdownCopy.value.isEmpty()) && action.text != "delete" && countdownCopy.value.length <= 5){
                                    countdownCopy.value += action.text
                                }else if(action.text == "delete"){
                                    countdownCopy.value = countdownCopy.value.dropLast(1)

                                }
                            }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false

                        var horasD = horas(countdownCopy).toInt()
                        var minutosD = minutos(countdownCopy).toInt()
                        var segundosD = segundos(countdownCopy).toInt()

                        horasD += minutosD/60
                        minutosD += segundosD/60

                        minutosD %= 60
                        segundosD %= 60

                        horas.value = horasD
                        minutos.value = minutosD
                        segundos.value = segundosD

                    },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onSurface)
                ) {
                    Text("Confirmar", style = MaterialTheme.typography.titleSmall)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text(
                        "Cancelar",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )
    }
}