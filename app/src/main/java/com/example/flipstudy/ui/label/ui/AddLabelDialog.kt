package com.example.flipstudy.ui.label.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Label
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flipstudy.ui.components.CountdownButton
import com.example.flipstudy.ui.screens.horas
import com.example.flipstudy.ui.screens.minutos
import com.example.flipstudy.ui.screens.segundos

@Composable
fun AddLabelDialog(openLabelDialog: MutableState<Boolean>) {

    if(openLabelDialog.value){

        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                openLabelDialog.value = false
            },
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier.fillMaxWidth(),
            text = {

                TextField(
                    value = "hey",
                    onValueChange = { /*TODO*/ },
                    colors = TextFieldDefaults.colors(MaterialTheme.colorScheme.onSecondaryContainer)
                )

            },
            confirmButton = {
                TextButton(
                    onClick = {

                    },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onSurface)
                ) {
                    Text("Confirmar", style = MaterialTheme.typography.titleSmall)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openLabelDialog.value = false
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