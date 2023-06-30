package com.example.flipstudy.ui.label.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsEndWidth
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Summarize
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ChipElevation
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flipstudy.ui.components.CountdownButton
import com.example.flipstudy.ui.label.data.Label
import kotlin.time.Duration

val assistives = listOf<Label>(
    Label(name = "Hey", color = Color.Yellow, dedicatedTime = Duration.ZERO),
    Label(name = "Hey", color = Color.Blue, dedicatedTime = Duration.ZERO),
    Label(name = "Hey", color = Color.Red, dedicatedTime = Duration.ZERO),
    Label(name = "Hey", color = Color.Green, dedicatedTime = Duration.ZERO),
    Label(name = "Hey", color = Color.Gray, dedicatedTime = Duration.ZERO),
    Label(name = "Hey", color = Color.Black, dedicatedTime = Duration.ZERO),
    Label(name = "Hey", color = Color.White, dedicatedTime = Duration.ZERO),
    Label(name = "Hey", color = Color.Magenta, dedicatedTime = Duration.ZERO),
    Label(name = "Hey", color = Color.Cyan, dedicatedTime = Duration.ZERO),
    Label(name = "Hey", color = Color.Yellow, dedicatedTime = Duration.ZERO),
    Label(name = "Hey", color = Color.Blue, dedicatedTime = Duration.ZERO),
    Label(name = "Hey", color = Color.Red, dedicatedTime = Duration.ZERO),
    Label(name = "Hey", color = Color.Green, dedicatedTime = Duration.ZERO),
    Label(name = "Hey", color = Color.Gray, dedicatedTime = Duration.ZERO),
    Label(name = "Hey", color = Color.Black, dedicatedTime = Duration.ZERO),
    Label(name = "Hey", color = Color.White, dedicatedTime = Duration.ZERO),
    Label(name = "Hey", color = Color.Magenta, dedicatedTime = Duration.ZERO),
    Label(name = "Hey", color = Color.Cyan, dedicatedTime = Duration.ZERO)

)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheet(openModalBottomSheet: MutableState<Boolean>) {

    var listsize = assistives.size

    val openLabelDialog = rememberSaveable { mutableStateOf(false) }

    if (openModalBottomSheet.value) {

        AddLabelDialog(openLabelDialog)

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
                LazyColumn(
                    userScrollEnabled = true,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .navigationBarsPadding(),
                ) {
                    itemsIndexed(assistives) { index, assistives ->
                        Box(
                            modifier = Modifier
                                .height(75.dp)
                                .clickable { /*TODO*/ },
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Row() {
                                    Icon(
                                        Icons.Filled.Label,
                                        contentDescription = "Localized description",
                                        tint = assistives.color,
                                        modifier = Modifier.absolutePadding(left = 12.dp)
                                    )
                                    Text(
                                        assistives.name + "$index",
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.absolutePadding(left = 12.dp)
                                    )
                                }
                                if(false) { // TODO
                                    Icon(
                                        Icons.Filled.Check,
                                        contentDescription = "Localized description",
                                        tint = MaterialTheme.colorScheme.outline,
                                        modifier = Modifier.absolutePadding(right = 12.dp)
                                    )
                                }

                            }
                        }
                        Divider(modifier = Modifier.fillMaxWidth())
                        // AÑADIR LABEL
                        if(index == listsize-1){
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
                                    Row() {
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
                }
            }
        )
    }

}

@Preview
@Composable
fun modalpreview() {

    var modal = remember { mutableStateOf(true) }

    ModalBottomSheet(modal)
}