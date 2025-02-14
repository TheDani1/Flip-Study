package com.example.flipstudy.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.time.Duration.Companion.seconds

fun <E> List<E>.extractProportions(selector: (E) -> Float): List<Float> {
    val total = this.sumOf { selector(it).toDouble() }
    return this.map { (selector(it) / total).toFloat() }
}

/**
 * Función composable encargada de renderizar el propio cuerpo de las estadísticas.
 *
 * @param items Listado de items
 * @param colors Colores a mostrar
 * @param amounts Cantidades
 * @param amountsTotal Cantidad total
 * @param circleLabel Label mostrada dentro del círculo
 * @param rows Filas generadas
 *
 */
@Composable
fun <T> CuerpoStatistics(
    items: List<T>,
    colors: (T) -> Color,
    amounts: (T) -> Int,
    amountsTotal: Int,
    circleLabel: String,
    rows: @Composable (T) -> Unit
) {
    Column {
        Box(Modifier.padding(16.dp)) {
            val accountsProportion = items.extractProportions { amounts(it).toFloat() }
            val circleColors = items.map { colors(it) }
            AnimatedCircle(
                accountsProportion,
                circleColors,
                Modifier
                    .height(300.dp)
                    .align(Alignment.Center)
                    .fillMaxWidth()
            )
            Column(modifier = Modifier.align(Alignment.Center)) {
                Text(
                    text = circleLabel,
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = amountsTotal.seconds.toString(),
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
        Spacer(Modifier.height(10.dp))
        Card(
            shape = CutCornerShape(0.dp),
            modifier = Modifier.fillMaxHeight()
        ) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                items.forEach { item ->
                    rows(item)
                }
            }
        }
    }
}

@Composable
fun <T> CuerpoStatisticsLandscape(
    items: List<T>,
    colors: (T) -> Color,
    amounts: (T) -> Int,
    amountsTotal: Int,
    circleLabel: String,
    rows: @Composable (T) -> Unit
) {

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(start = 20.dp, top = 20.dp, bottom = 20.dp)
    ) {
        Box() {
            val accountsProportion = items.extractProportions { amounts(it).toFloat() }
            val circleColors = items.map { colors(it) }
            AnimatedCircle(
                accountsProportion,
                circleColors,
                Modifier
                    .height(300.dp)
                    .align(Alignment.Center)
                    .fillMaxWidth()
            )
            Column(modifier = Modifier.align(Alignment.Center)) {
                Text(
                    text = circleLabel,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = amountsTotal.seconds.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                items.forEach { item ->
                    rows(item)
                }
            }

            /*Column(
                modifier = Modifier
                    .padding(12.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                items.forEach { item ->
                    rows(item)
                }
            }*/
        }


    }



}