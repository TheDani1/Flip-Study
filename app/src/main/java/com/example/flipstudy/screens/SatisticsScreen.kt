package com.example.flipstudy.screens

import android.graphics.Insets
import android.graphics.Typeface
import android.graphics.fonts.FontFamily
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
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
import com.example.flipstudy.statistics.StatisticViewModel
import com.example.flipstudy.statistics.components.GoalRow
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.endAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.component.lineComponent
import com.patrykandpatrick.vico.compose.component.overlayingComponent
import com.patrykandpatrick.vico.compose.component.shape.shader.fromBrush
import com.patrykandpatrick.vico.compose.component.shapeComponent
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.compose.dimensions.dimensionsOf
import com.patrykandpatrick.vico.compose.legend.legendItem
import com.patrykandpatrick.vico.compose.legend.verticalLegend
import com.patrykandpatrick.vico.compose.style.ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.compose.style.currentChartStyle
import com.patrykandpatrick.vico.core.DefaultAlpha
import com.patrykandpatrick.vico.core.DefaultColors
import com.patrykandpatrick.vico.core.DefaultDimens
import com.patrykandpatrick.vico.core.DefaultDimens.AXIS_LABEL_ROTATION_DEGREES
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.chart.column.ColumnChart
import com.patrykandpatrick.vico.core.chart.composed.plus
import com.patrykandpatrick.vico.core.chart.decoration.ThresholdLine
import com.patrykandpatrick.vico.core.chart.dimensions.HorizontalDimensions
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.component.marker.MarkerComponent
import com.patrykandpatrick.vico.core.component.shape.DashedShape
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.ShapeComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.shape.cornered.Corner
import com.patrykandpatrick.vico.core.component.shape.cornered.MarkerCorneredShape
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.context.MeasureContext
import com.patrykandpatrick.vico.core.entry.ChartEntryModel
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.composed.plus
import com.patrykandpatrick.vico.core.entry.entriesOf
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.extension.copyColor
import com.patrykandpatrick.vico.core.marker.DefaultMarkerLabelFormatter
import com.patrykandpatrick.vico.core.marker.Marker
import com.patrykandpatrick.vico.views.chart.line.lineChart
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import kotlin.random.Random


/**
 * Función de ayuda para proveer de estilos al Chart
 *
 */
@Composable
internal fun rememberChartStyle(columnChartColors: List<Color>, lineChartColors: List<Color>): ChartStyle {
    val isSystemInDarkTheme = isSystemInDarkTheme()
    return remember(columnChartColors, lineChartColors, isSystemInDarkTheme) {
        val defaultColors = if (isSystemInDarkTheme) DefaultColors.Dark else DefaultColors.Light
        ChartStyle(
            ChartStyle.Axis(
                axisLabelColor = Color(defaultColors.axisLabelColor),
                axisGuidelineColor = Color(defaultColors.axisGuidelineColor),
                axisLineColor = Color(defaultColors.axisLineColor),
            ),
            ChartStyle.ColumnChart(
                columnChartColors.map { columnChartColor ->
                    LineComponent(
                        columnChartColor.toArgb(),
                        DefaultDimens.COLUMN_WIDTH,
                        Shapes.roundedCornerShape(DefaultDimens.COLUMN_ROUNDNESS_PERCENT),
                    )
                },
            ),
            ChartStyle.LineChart(
                lineChartColors.map { lineChartColor ->
                    LineChart.LineSpec(
                        lineColor = lineChartColor.toArgb(),
                        lineBackgroundShader = DynamicShaders.fromBrush(
                            Brush.verticalGradient(
                                listOf(
                                    lineChartColor.copy(DefaultAlpha.LINE_BACKGROUND_SHADER_START),
                                    lineChartColor.copy(DefaultAlpha.LINE_BACKGROUND_SHADER_END),
                                ),
                            ),
                        ),
                    )
                },
            ),
            ChartStyle.Marker(),
            Color(defaultColors.elevationOverlayColor),
        )
    }
}

/**
 * Función de ayuda y soporte para el marcador del Chart
 *
 */
@Composable
internal fun rememberMarker(): Marker {
    val labelBackgroundColor = MaterialTheme.colorScheme.surface
    val labelBackground = remember(labelBackgroundColor) {
        ShapeComponent(MarkerCorneredShape(Corner.FullyRounded), labelBackgroundColor.toArgb()).setShadow(
            radius = 4f,
            dy = 2f,
            applyElevationOverlay = true,
        )
    }
    val label = textComponent(
        background = labelBackground,
        lineCount = 1,
        padding = dimensionsOf(8.dp, 4.dp),
    )
    val indicatorInnerComponent = shapeComponent(Shapes.pillShape, MaterialTheme.colorScheme.surface)
    val indicatorCenterComponent = shapeComponent(Shapes.pillShape, Color.White)
    val indicatorOuterComponent = shapeComponent(Shapes.pillShape, Color.White)
    val indicator = overlayingComponent(
        outer = indicatorOuterComponent,
        inner = overlayingComponent(
            outer = indicatorCenterComponent,
            inner = indicatorInnerComponent,
            innerPaddingAll = 5.dp,
        ),
        innerPaddingAll = 10.dp,
    )
    val guideline = lineComponent(
        MaterialTheme.colorScheme.onSurface.copy(.2f),
        2.dp,
        DashedShape(Shapes.pillShape, 8f, 4f),
    )
    return remember(label, indicator, guideline) {
        object : MarkerComponent(label, indicator, guideline) {
            init {
                indicatorSizeDp = 36f
                onApplyEntryColor = { entryColor ->
                    indicatorOuterComponent.color = entryColor.copyColor(32)
                    with(indicatorCenterComponent) {
                        color = entryColor
                        setShadow(radius = 12f, color = entryColor)
                    }
                }
            }

            @RequiresApi(Build.VERSION_CODES.Q)
            override fun getInsets(
                context: MeasureContext,
                outInsets: com.patrykandpatrick.vico.core.chart.insets.Insets,
                horizontalDimensions: HorizontalDimensions
            ) = with(context) {
                outInsets.top = label.getHeight(context) + MarkerCorneredShape(Corner.FullyRounded).tickSizeDp.pixels +
                        4f.pixels * 1.3f -
                        2f.pixels
            }
        }
    }
}


/**
 * Función encargada de renderizar una página en blanco para simbolizar la presencia de datos nulos o vacíos.
 *
 */
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

/**
 * Función encargada de renderizar una página en blanco para simbolizar la presencia de datos nulos o vacíos.
 *
 */
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

/**
 * Función encargada de renderizar la página de las estadísticas.
 *
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(orientation: Int, goalsPreferences: GoalsPreferences, statisticViewModel: StatisticViewModel) {

    val context = LocalContext.current

    val format = SimpleDateFormat("dd MMM yyyy",  Locale.getDefault())
    val calendar = Calendar.getInstance(Locale("es","ES"))
    calendar.firstDayOfWeek = Calendar.MONDAY

    val dailyGoal = rememberSaveable { mutableStateOf(goalsPreferences.get("dailyGoal", 1800)) }
    val weekGoal = rememberSaveable { mutableStateOf(goalsPreferences.get("weekGoal", 1800)) }
    val monthlyGoal = rememberSaveable { mutableStateOf(goalsPreferences.get("monthlyGoal", 1800)) }
    val yearGoal = rememberSaveable { mutableStateOf(goalsPreferences.get("yearGoal", 1800)) }

    val goals = rememberSaveable { mutableListOf(dailyGoal, weekGoal, monthlyGoal, yearGoal) }

    val goalsTitles = listOf("Daily Goal", "Weekly Goal", "Monthly Goal", "Yearly Goal")

    val state = remember { mutableStateOf(0) }
    val titles = listOf("Goals", "History")

    val horizontalAxisValueFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, _ ->
        when(value){
            0F -> "Lunes"
            1F -> "Martes"
            2F -> "Miercoles"
            3F -> "Jueves"
            4F -> "Viernes"
            5F -> "Sabado"
            6F -> "Domingo"
            else -> "Otro dia"
        }
    }

    val verticalAxisValueFormatter = AxisValueFormatter<AxisPosition.Vertical.Start> { value, _ ->
        DateUtils.fromSecondsToTime(value.toInt())
    }

    val columns = listOf(
        Color.Red,
        Color.Blue,
        Color.Green,
        Color.Yellow,
        Color.Magenta,
        Color.Green
    )
    
    val model : ChartEntryModel by statisticViewModel.model.observeAsState(initial = entryModelOf(2))
    val colors by statisticViewModel.colors.observeAsState(initial = emptyList())
    val colorsLabelled by statisticViewModel.colorsLabelled.observeAsState(initial = emptyList())

    @Composable
    fun rememberLegend() = verticalLegend(
        items = statisticViewModel.columns.mapIndexed { index, chartColor ->
            legendItem(
                icon = shapeComponent(Shapes.pillShape, chartColor),
                label = textComponent(
                    color = currentChartStyle.axis.axisLabelColor,
                    textSize = 12.sp,
                ),
                labelText = statisticViewModel.stringColumns[index],
            )
        },
        iconSize = 8.dp,
        iconPadding = 10.dp,
        spacing = 4.dp,
        padding = dimensionsOf(top = 8.dp),
    )

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

            if(statisticViewModel.goalsSameWeek.isNotEmpty()){

                val line = shapeComponent(DashedShape(Shapes.rectShape, 8f, 4f),strokeWidth = 2.dp, strokeColor = Color.Black)
                val label = textComponent(
                    color = Color.White,
                    background = shapeComponent(Shapes.pillShape, Color.Black),
                    padding =  dimensionsOf(8.dp, 2.dp),
                    margins = dimensionsOf(4.dp),
                    typeface = Typeface.MONOSPACE,
                )

                Surface(modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)) {

                    val thresholdLine = ThresholdLine(thresholdValue = dailyGoal.value.toFloat(), lineComponent = line, labelComponent = label, thresholdLabel = DateUtils.fromSecondsToTime(dailyGoal.value))

                    ProvideChartStyle(rememberChartStyle(statisticViewModel.columns, statisticViewModel.columns)) {
                        val marker = rememberMarker()
                        val columnChart = columnChart(
                            mergeMode = ColumnChart.MergeMode.Stack,
                            targetVerticalAxisPosition = AxisPosition.Vertical.Start,
                            decorations = remember(thresholdLine) { listOf(thresholdLine) }
                        )
                        Chart(
                            chart = columnChart ,
                            startAxis = startAxis(guideline = null, maxLabelCount = 4, valueFormatter = verticalAxisValueFormatter),
                            bottomAxis = bottomAxis(labelRotationDegrees = 90F, valueFormatter = horizontalAxisValueFormatter),
                            model = model,
                            marker = marker,
                            legend = rememberLegend()
                        )
                    }
                }

            }else{

                Column(modifier = Modifier.fillMaxSize().padding(50.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically)) {
                    Image(painter = painterResource(id = R.drawable.undraw_no_data_re_kwbl), "No data")

                    Text(text = "No data", style = MaterialTheme.typography.displayLarge)
                }


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