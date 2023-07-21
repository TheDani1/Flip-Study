package com.example.flipstudy.screens

import android.graphics.fonts.FontFamily
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.SportsScore
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flipstudy.R
import com.example.flipstudy.label.data.Label
import com.example.flipstudy.label.data.LabelDatabase
import com.example.flipstudy.statistics.DateUtils
import com.example.flipstudy.statistics.GoalsPreferences
import com.example.flipstudy.statistics.Statistic
import com.example.flipstudy.statistics.components.GoalRow
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.component.lineComponent
import com.patrykandpatrick.vico.core.chart.column.ColumnChart
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entriesOf
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.views.chart.line.lineChart
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.random.Random

@Composable
fun BlankScreenLandscape() {

    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Row(modifier = Modifier.align(Alignment.Center)) {

            Icon(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "image description",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(200.dp),
                tint = MaterialTheme.colorScheme.surfaceTint
            )

            Text(
                text = stringResource(R.string.flip_and_study_more),
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.surfaceTint,
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = stringResource(R.string.no_statistics_data),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.surfaceTint,
                textAlign = TextAlign.Center
            )

        }

    }

}

@Preview
@Composable
fun BlankScreen() {

    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Column(modifier = Modifier.align(Alignment.Center)) {

            Icon(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "image description",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(200.dp),
                tint = MaterialTheme.colorScheme.surfaceTint
            )

            Text(
                text = stringResource(R.string.flip_and_study_more),
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.surfaceTint,
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = stringResource(R.string.no_statistics_data),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.surfaceTint,
                textAlign = TextAlign.Center
            )

        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(db: LabelDatabase, orientation: Int, goalsPreferences: GoalsPreferences) {

    val reals = remember {
        mutableStateListOf<Statistic>().apply {
            addAll(db.statisticDao().getAllStatistics())
        }
    }

    val context = LocalContext.current

    val format = SimpleDateFormat("dd MMM yyyy",  Locale.getDefault())

    val dailyGoal = rememberSaveable { mutableStateOf(goalsPreferences.get("dailyGoal", 1800)) }
    val weekGoal = rememberSaveable { mutableStateOf(goalsPreferences.get("weekGoal", 1800)) }
    val monthlyGoal = rememberSaveable { mutableStateOf(goalsPreferences.get("monthlyGoal", 1800)) }
    val yearGoal = rememberSaveable { mutableStateOf(goalsPreferences.get("yearGoal", 1800)) }

    val goals = rememberSaveable { mutableListOf(dailyGoal, weekGoal, monthlyGoal, yearGoal) }

    val goalsTitles = listOf("Daily Goal", "Weekly Goal", "Monthly Goal", "Yearly Goal")

    val state = remember { mutableStateOf(0) }
    val titles = listOf("Goals", "History", "Por etiqueta")
    Column {
        TabRow(selectedTabIndex = state.value) {
            titles.forEachIndexed { index, title ->
                Tab(
                    selected = state.value == index,
                    onClick = { state.value = index },
                    text = {

                        if(title == "Goals"){
                            Icon(Icons.Default.SportsScore, contentDescription = "Goals tab")
                        }else{
                            Text(text = title, maxLines = 2, overflow = TextOverflow.Ellipsis)
                        }

                    }
                )
            }
        }

        if (state.value == 0) {

            LazyColumn(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(36.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally) {
                itemsIndexed(goals){
                    index, goal ->
                    GoalRow(text = goalsTitles[index], goal = goal)
                }

                item {
                    Button(onClick = {
                        goalsPreferences.set("dailyGoal", dailyGoal.value)
                        goalsPreferences.set("weekGoal", weekGoal.value)
                        goalsPreferences.set("monthlyGoal", monthlyGoal.value)
                        goalsPreferences.set("yearGoal", yearGoal.value)
                    },
                    ) { Text("Save goals", style = MaterialTheme.typography.titleLarge) }
                }

            }

        }else if(state.value == 1){

            val model = entryModelOf(
                entriesOf(2f, -1f, -4f, 2f, 1f, -5f, -2f, -3f),
                entriesOf(3f, -2f, 2f, -1f, 2f, -3f, -4f, -1f),
                entriesOf(1f, -2f, 2f, 1f, -1f, 4f, 4f, -2f),
            )

            val columns: List<LineComponent> = listOf(
                lineComponent(color = Color(0xFF494949), thickness = 8.dp),
                lineComponent(color = Color(0xFF7C7A7A), thickness = 8.dp),
                lineComponent(color = Color(0xFFFF5D73), thickness = 8.dp),
            )

            Surface {
                Chart(
                    modifier = Modifier.height(250.dp),
                    chart = columnChart(
                        columns = columns,
                        mergeMode = ColumnChart.MergeMode.Stack,
                    ),
                    model = model,
                    startAxis = startAxis(maxLabelCount = 8),
                    bottomAxis = bottomAxis(),
                )
            }

        }

        reals.forEach {
            statistic ->
            Column {
                Text(text = statistic.id.toString())
                Text(text = format.format(statistic.timestamp))
                Text(text = format.format(System.currentTimeMillis()))
                Text(text = System.currentTimeMillis().toString())
                Text(text = statistic.labelId.toString())
                Text(text = statistic.dedicatedSeconds.toString())
            }

        }
    }

    //val amountsTotal = remember { reals.sumOf { label -> label.dedicatedSeconds } }

    /*if(orientation == Configuration.ORIENTATION_LANDSCAPE){

        if (amountsTotal <= 0) {

            BlankScreenLandscape()

        }else {

            CuerpoStatisticsLandscape(
                items = reals,
                colors = { label -> colorEnumToColor(label.color) },
                amounts = { label -> label.dedicatedSeconds },
                amountsTotal = amountsTotal,
                circleLabel = "Total"
            ) { label ->
                Row {
                    Box(
                        modifier = Modifier
                            .height(75.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Row {
                                Icon(
                                    Icons.Filled.Label,
                                    contentDescription = "Localized description",
                                    tint = colorEnumToColor(label.color),
                                    modifier = Modifier.absolutePadding(left = 12.dp)
                                )
                                Text(
                                    label.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.absolutePadding(left = 12.dp)
                                )

                                Text(
                                    label.dedicatedSeconds.seconds.toString(),
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.absolutePadding(left = 12.dp)
                                )
                            }

                        }
                    }
                }
                //Divider(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.outline)
            }
        }

    }else {

        if (amountsTotal <= 0) {

            BlankScreen()

        }else {

            CuerpoStatistics(
                items = reals,
                colors = { label -> colorEnumToColor(label.color) },
                amounts = { label -> label.dedicatedSeconds },
                amountsTotal = amountsTotal,
                circleLabel = "Total"
            ) { label ->
                Row {
                    Box(
                        modifier = Modifier
                            .height(75.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Row {
                                Icon(
                                    Icons.Filled.Label,
                                    contentDescription = "Localized description",
                                    tint = colorEnumToColor(label.color),
                                    modifier = Modifier.absolutePadding(left = 12.dp)
                                )
                                Text(
                                    label.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.absolutePadding(left = 12.dp)
                                )

                                Text(
                                    label.dedicatedSeconds.seconds.toString(),
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.absolutePadding(left = 12.dp)
                                )
                            }

                        }
                    }
                }
                //Divider(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.outline)
            }
        }

    }*/

}