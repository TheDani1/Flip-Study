package com.example.flipstudy.ui

import androidx.annotation.Keep
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.flipstudy.R

@Keep
class Botones(
    val id:Int,
    var name: String? = "0",
    val a: (Int) -> Int = { i: Int -> i + 1 }
){}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TimerScreen() {

    val checkedState = remember { mutableStateOf(true) }
    var checked by remember { mutableStateOf(true) }
    val openDialog = remember { mutableStateOf(false) }

    var numbers = (1..9).toList()
    numbers += 0

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                openDialog.value = false
            },
            modifier = Modifier.fillMaxWidth(),
            icon = { Icon(Icons.Filled.HourglassEmpty, contentDescription = null) },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "00",
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "h",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    Text(
                        text = "00",
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "m",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    Text(
                        text = "00",
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "s",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            },
            text = {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(horizontal = 1.dp, vertical = 1.dp),
                    verticalArrangement = Arrangement.spacedBy(7.dp),
                    horizontalArrangement = Arrangement.spacedBy(7.dp),
                ) {
                    items(numbers) { item ->

                        Button(
                            onClick = { /*TODO*/ },
                            modifier = Modifier
                                .fillMaxSize()
                                .size(75.dp),  //avoid the oval shape
                            shape = CircleShape,
                            contentPadding = PaddingValues(0.dp),  //avoid the little icon
                        ) {
                            Text(
                                text = item.toString(),
                                style = MaterialTheme.typography.titleLarge
                            )
                        }

                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
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


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp), horizontalAlignment = Alignment.End
    ) {

        Icon(
            painter = painterResource(id = R.drawable.subtract),
            contentDescription = "image description",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(200.dp),
            tint = MaterialTheme.colorScheme.surfaceTint
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 5.dp, shape = AbsoluteRoundedCornerShape(20.dp))
                .fillMaxHeight()
                .background(color = Color(0xFFFFFBFE), shape = RoundedCornerShape(size = 20.dp)),
            shape = AbsoluteRoundedCornerShape(20.dp)
        ) {
            Box(Modifier.fillMaxSize()) {

                Column(
                    Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp)
                ) {

                    TextButton(
                        onClick = { openDialog.value = true },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = "00:00",
                            style = MaterialTheme.typography.displayLarge,


                            )

                    }

                    Divider()

                    Row(
                        modifier = Modifier.padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.size(35.dp),
                            imageVector = Icons.Filled.Label,
                            contentDescription = "Etiqueta",
                            tint = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = "Matemáticas",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(horizontal = 5.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            modifier = Modifier.size(35.dp),
                            imageVector = Icons.Filled.Vibration,
                            contentDescription = "Vibrar"
                        )
                        Text(
                            text = "Vibrar",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(horizontal = 5.dp)
                        )

                        Checkbox(
                            checked = checkedState.value,
                            onCheckedChange = { checkedState.value = it }
                        )
                    }


                    val icon: (@Composable () -> Unit)? = if (checked) {
                        {
                            Icon(
                                imageVector = Icons.Filled.HourglassTop,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize),
                            )
                        }
                    } else {
                        null
                    }

                    Switch(
                        modifier = Modifier.semantics { contentDescription = "Demo with icon" },
                        checked = checked,
                        onCheckedChange = { checked = it },
                        thumbContent = icon
                    )

                    Text(
                        text = "Turn the cell phone upside down to start the countdown",
                        style = MaterialTheme.typography.displaySmall,
                        modifier = Modifier.padding(horizontal = 5.dp)
                    )


                }
            }


        }


    }


}