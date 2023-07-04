package com.example.flipstudy.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Label
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flipstudy.R
import com.example.flipstudy.ui.components.CuerpoStatistics
import com.example.flipstudy.ui.label.data.Label
import com.example.flipstudy.ui.label.data.LabelDatabase
import com.example.flipstudy.ui.label.data.colorEnumToColor
import kotlin.time.Duration.Companion.seconds

@Preview
@Composable
fun BlankScreen() {

    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)) {

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

@Composable
fun StatisticsScreen(db: LabelDatabase) {

    val reals = remember { mutableStateListOf<Label>().apply {
        addAll(db.labelDao().getAllLabels())
    } }

    Log.d("STATISTICS", reals.sumOf { label -> label.dedicatedSeconds }.toString())

    val amountsTotal = remember { reals.sumOf { label -> label.dedicatedSeconds } }

    if (amountsTotal <= 0) {

        BlankScreen()

    } else {

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

}