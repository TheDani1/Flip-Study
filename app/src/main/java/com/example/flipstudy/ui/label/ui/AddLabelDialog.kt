package com.example.flipstudy.ui.label.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Label
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.example.flipstudy.ui.label.data.Label
import com.example.flipstudy.ui.label.data.LabelDatabase
import com.example.flipstudy.ui.label.data.colorToEnumColor
import kotlinx.coroutines.launch

val availableColours = listOf(
    Color.Red,
    Color.Blue,
    Color.Gray,
    Color.Green,
    Color.Cyan,
    Color.Yellow,
    Color.Magenta,
    Color.White,
    Color.Black
)

@Composable
fun AddLabelDialog(
    openLabelDialog: MutableState<Boolean>,
    assistives: SnapshotStateList<Label>,
    db: LabelDatabase
) {

    val coroutineScope = rememberCoroutineScope()

    val nombreLabel = remember { mutableStateOf("") }

    val colorSelected = remember { mutableStateOf(Color.Black) }

    if (openLabelDialog.value) {

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

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    OutlinedTextField(
                        value = nombreLabel.value,
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                        placeholder = { Text(text = "New label name") },
                        onValueChange = { nombreLabel.value = it },
                        leadingIcon = {
                            Icon(
                                Icons.Filled.Label,
                                contentDescription = "Localized description",
                                tint = colorSelected.value,
                            )
                        },
                        textStyle = MaterialTheme.typography.titleMedium
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(40.dp), userScrollEnabled = false,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(availableColours) { color ->

                            ElevatedButton(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .clip(CircleShape),
                                content = {},
                                colors = ButtonDefaults.buttonColors(color),
                                onClick = { colorSelected.value = color },
                                elevation = ButtonDefaults.buttonElevation(2.dp)
                            )
                        }
                    }

                }
            },
            confirmButton = {
                TextButton(
                    onClick = {

                        val labelInsert = Label(0, nombreLabel.value, colorToEnumColor(colorSelected.value), 0)

                        coroutineScope.launch {
                            db.labelDao().insert(labelInsert)
                        }

                        assistives.add(labelInsert)

                        colorSelected.value = Color.Black
                        nombreLabel.value = ""
                        openLabelDialog.value = false

                    },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onSurface)
                ) {
                    Text("Confirmar", style = MaterialTheme.typography.titleSmall)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        colorSelected.value = Color.Black
                        nombreLabel.value = ""
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