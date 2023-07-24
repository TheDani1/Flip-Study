package com.example.flipstudy.label.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.Summarize
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.flipstudy.R
import com.example.flipstudy.label.data.Label
import com.example.flipstudy.label.data.LabelDatabase
import com.example.flipstudy.label.data.colorEnumToColor
import kotlinx.coroutines.launch

/**
 * Función [Composable] que renderiza un modal como base.
 *
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheet(
    openModalBottomSheet: MutableState<Boolean>,
    db: LabelDatabase,
    labelSelected: MutableState<Label>
) {

    val coroutineScope = rememberCoroutineScope()

    val assistives = remember { mutableStateListOf(db.labelDao().getAllLabels().toMutableList()) }

    val reals = remember { mutableStateListOf<Label>().apply {
        addAll(db.labelDao().getAllLabels())
    } }

    val openLabelDialog = rememberSaveable { mutableStateOf(false) }

    if (openModalBottomSheet.value) {

        AddLabelDialog(openLabelDialog, reals, db)

        androidx.compose.material3.ModalBottomSheet(
            onDismissRequest = {
                openModalBottomSheet.value = false
            },
            shape = CutCornerShape(0.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier
                .fillMaxHeight(),
            content = {

                if (assistives.isNotEmpty()) {

                    LazyColumn(
                        userScrollEnabled = true,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .navigationBarsPadding(),
                    ) {
                        itemsIndexed(reals) { _, real ->
                            Box(
                                modifier = Modifier
                                    .height(75.dp)
                                    .clickable { labelSelected.value = real
                                               openModalBottomSheet.value = false},
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Row {

                                        if(labelSelected.value.id == real.id) {
                                            Icon(
                                                Icons.Filled.Check,
                                                contentDescription = "Label utilizada",
                                                tint = MaterialTheme.colorScheme.outline,
                                                modifier = Modifier.absolutePadding(left = 12.dp)
                                            )
                                        }

                                        Icon(
                                            Icons.Filled.Label,
                                            contentDescription = "Label",
                                            tint = colorEnumToColor(real.color),
                                            modifier = Modifier.absolutePadding(left = 6.dp)
                                        )
                                        Text(
                                            real.name,
                                            style = MaterialTheme.typography.titleMedium,
                                            modifier = Modifier.absolutePadding(left = 12.dp)
                                        )
                                    }
                                    IconButton(
                                        content = {
                                            Icon(
                                                Icons.Filled.Delete,
                                                contentDescription = "Localized description",
                                                modifier = Modifier.absolutePadding(right = 12.dp)
                                            )
                                        },
                                        onClick = {
                                            coroutineScope.launch {
                                                db.labelDao().delete(
                                                    Label(
                                                        real.id,
                                                        real.name,
                                                        real.color,
                                                        //real.dedicatedSeconds
                                                    )
                                                )
                                            }
                                            reals.remove(real)
                                        }
                                    )
                                }
                            }
                            Divider(modifier = Modifier.fillMaxWidth())
                        }
                        item {
                            Divider(modifier = Modifier.fillMaxWidth())
                            // AÑADIR LABEL

                            Box(
                                modifier = Modifier
                                    .height(75.dp)
                                    .clickable { openLabelDialog.value = true },
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Row {
                                        Icon(
                                            Icons.Filled.Summarize,
                                            contentDescription = "Localized description",
                                            modifier = Modifier.absolutePadding(left = 12.dp)
                                        )
                                        Text(
                                            text = stringResource(R.string.add_label),
                                            style = MaterialTheme.typography.titleMedium,
                                            modifier = Modifier.absolutePadding(left = 12.dp)
                                        )
                                    }
                                }
                            }

                        }
                    }
                }else{
                    Box(
                        modifier = Modifier
                            .height(75.dp)
                            .clickable { openLabelDialog.value = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Row {
                                Icon(
                                    Icons.Filled.Summarize,
                                    contentDescription = "Localized description",
                                    modifier = Modifier.absolutePadding(left = 12.dp)
                                )
                                Text(
                                    text = "Add label",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.absolutePadding(left = 12.dp)
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}