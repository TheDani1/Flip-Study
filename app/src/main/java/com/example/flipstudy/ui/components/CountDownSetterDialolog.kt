package com.example.flipstudy.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collection.MutableVector
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flipstudy.ui.screens.horas
import com.example.flipstudy.ui.screens.minutos
import com.example.flipstudy.ui.screens.segundos

@Composable
fun CountdownSetterDialog(
    openDialog: MutableState<Boolean>,
    actions: List<TimerAction>,
    modifier: Modifier = Modifier,
    countdown: MutableState<String>
) {

    if (openDialog.value) {

        var countdown_copy = remember { mutableStateOf(countdown.value) }

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
                            text = horas(countdown_copy),
                            style = MaterialTheme.typography.displayLarge,
                            color = if(countdown_copy.value.length >= 5 ) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "h",
                            style = MaterialTheme.typography.titleLarge,
                            color = if(countdown_copy.value.length >= 5 ) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = minutos(countdown_copy),
                            style = MaterialTheme.typography.displayLarge,
                            color = if(countdown_copy.value.length >= 3 ) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "m",
                            style = MaterialTheme.typography.titleLarge,
                            color = if(countdown_copy.value.length >= 3 ) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = segundos(countdown_copy),
                            style = MaterialTheme.typography.displayLarge,
                            color = if(countdown_copy.value.isNotEmpty()) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "s",
                            style = MaterialTheme.typography.titleLarge,
                            color = if(countdown_copy.value.isNotEmpty()) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurfaceVariant
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
                                if(!(action.text == "0" && countdown_copy.value.isEmpty()) && action.text != "delete" && countdown_copy.value.length <= 5){
                                    countdown_copy.value += action.text
                                }else if(action.text == "delete"){
                                    countdown_copy.value = countdown_copy.value.dropLast(1)

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

                        var horas = horas(countdown_copy).toInt()
                        var minutos = minutos(countdown_copy).toInt()
                        var segundos = segundos(countdown_copy).toInt()

                        var horasString = ""
                        var minutosString = ""
                        var segundosString = ""

                        horas += minutos/60
                        minutos += segundos/60

                        minutos %= 60
                        segundos %= 60

                        horasString = if(horas < 9){
                            "0$horas"
                        }else{
                            horas.toString()
                        }

                        minutosString = if(minutos < 9){
                            "0$minutos"

                        }else{
                            minutos.toString()
                        }

                        segundosString = if(segundos < 9){
                            "0$segundos"
                        }else{
                            segundos.toString()
                        }

                        countdown.value = horasString+minutosString+segundosString
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