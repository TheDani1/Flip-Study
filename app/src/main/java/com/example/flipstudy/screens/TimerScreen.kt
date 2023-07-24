package com.example.flipstudy.screens

import android.content.res.Configuration
import android.media.Ringtone
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flipstudy.R
import com.example.flipstudy.components.CountdownActions
import com.example.flipstudy.components.CountdownSetterDialog
import com.example.flipstudy.components.SegmentedButton
import com.example.flipstudy.label.data.ColorEnum
import com.example.flipstudy.label.data.Label
import com.example.flipstudy.label.data.LabelDatabase
import com.example.flipstudy.label.data.colorEnumToColor
import com.example.flipstudy.label.ui.ModalBottomSheet
import com.example.flipstudy.statistics.Statistic
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds


fun intsToCountdown(
    horas: MutableState<Int>,
    minutos: MutableState<Int>,
    segundos: MutableState<Int>,
    countdown: MutableState<String>
) {

    var copyh = horas.value
    var copym = minutos.value
    var copys = segundos.value

    copyh += copym / 60
    copym += copys / 60

    copym %= 60
    copys %= 60

    val horasString: String = if (copyh < 9) {
        "0$copyh"
    } else {
        copyh.toString()
    }

    val minutosString: String = if (copym < 9) {
        "0$copym"

    } else {
        copym.toString()
    }

    val segundosString: String = if (copys < 9) {
        "0$copys"
    } else {
        copys.toString()
    }

    countdown.value = horasString + minutosString + segundosString
}

fun segundos(tiempo: MutableState<String>): String {
    return if (tiempo.value.length >= 2) {
        tiempo.value.takeLast(2)
    } else if (tiempo.value.length == 1) {
        "0" + tiempo.value.takeLast(1)
    } else {
        "00"
    }
}

fun minutos(tiempo: MutableState<String>): String {
    return if (tiempo.value.length >= 4) {
        tiempo.value.takeLast(4).take(2)
    } else if (tiempo.value.length == 3) {
        "0" + tiempo.value.takeLast(3).take(1)
    } else {
        "00"
    }
}

fun horas(tiempo: MutableState<String>): String {
    return if (tiempo.value.length >= 6) {
        tiempo.value.takeLast(6).take(2)
    } else if (tiempo.value.length == 5) {
        "0" + tiempo.value.takeLast(5).take(1)
    } else {
        "00"
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun TimerScreen(
    db: LabelDatabase,
    sensorValues: Float,
    vibratorManager: Vibrator,
    ringtone: Ringtone,
    orientation: Int,
    rotationValues: Float,
) {

    val checkedVibration = rememberSaveable { mutableStateOf(true) }

    val checkSound = rememberSaveable { mutableStateOf(true) }

    val openDialog = rememberSaveable { mutableStateOf(false) }

    val countdown = rememberSaveable { mutableStateOf("000000") }

    val worked = rememberSaveable { mutableStateOf(false) }

    val horas = rememberSaveable { mutableStateOf(0) }
    val minutos = rememberSaveable { mutableStateOf(59) }
    val segundos = rememberSaveable { mutableStateOf(59) }

    // TODO
    val timings: LongArray = longArrayOf(50, 50, 50, 50, 50, 50, 50)
    val amplitudes: IntArray = intArrayOf(33, 0, 75, 0, 170, 0, 200)
    val repeatIndex = 1 // Do not repeat.

    // H -> 0
    // M -> 1
    // S -> 2
    val openModalBottomSheet = rememberSaveable { mutableStateOf(false) }

    val labelSelected =
        rememberSaveable { mutableStateOf(db.labelDao().getAllLabels().random()) }

    val dedicatedSecondsAccumulated = rememberSaveable { mutableStateOf(0) }

    val modeFlipStudy = rememberSaveable { mutableStateOf(true) }
    val countdownRunning = rememberSaveable { mutableStateOf(false) }

    // COUNTDOWN SETTER DIALOG
    CountdownSetterDialog(
        openDialog, actions = CountdownActions,
        modifier = Modifier.padding(8.dp), countdown, horas, minutos, segundos
    )

    // MODAL DE ETIQUETAS
    ModalBottomSheet(openModalBottomSheet = openModalBottomSheet, db, labelSelected)

    if (modeFlipStudy.value) {

        // COUNTDOWN EN SI
        LaunchedEffect(key1 = sensorValues <= 20f && rotationValues <= -9.81f, key2 = segundos.value) {

            if (segundos.value == 0 && minutos.value >= 1) {
                minutos.value -= 1
                segundos.value += 60
            }

            if (minutos.value == 0 && horas.value >= 1) {
                horas.value -= 1
                minutos.value += 60
            }

            if (minutos.value == 0 && segundos.value == 0 && horas.value == 0) {

                Log.d("BOOLEAN", checkedVibration.value.toString())

                if (checkedVibration.value) {
                    if (vibratorManager.areAllPrimitivesSupported() && checkedVibration.value) {
                        vibratorManager.vibrate(
                            VibrationEffect.createOneShot(
                                1000,
                                50
                            )
                        )
                    } else {
                        vibratorManager.vibrate(
                            VibrationEffect.createWaveform(
                                timings,
                                amplitudes,
                                repeatIndex
                            )
                        )
                    }
                }

                if (checkSound.value) ringtone.play()

                if (sensorValues > 20f) {
                    if (checkSound.value) ringtone.stop()
                    if (checkedVibration.value) vibratorManager.cancel()

                    db.statisticDao().insert(
                        Statistic(
                            labelId = labelSelected.value.id,
                            dedicatedSeconds = dedicatedSecondsAccumulated.value,
                            timestamp = System.currentTimeMillis()
                        )
                    )
                }

            }

            if (sensorValues > 20f && worked.value) {
                db.statisticDao().insert(
                    Statistic(
                        labelId = labelSelected.value.id,
                        dedicatedSeconds = dedicatedSecondsAccumulated.value,
                        timestamp = System.currentTimeMillis()
                    )
                )
                worked.value = false
            }

            if (sensorValues <= 20f && segundos.value > 0) {
                delay(1.seconds)
                segundos.value -= 1
                dedicatedSecondsAccumulated.value++
                worked.value = true
            }

        }

    } else {
        // COUNTDOWN EN SI
        LaunchedEffect(key1 = countdownRunning.value, key2 = segundos.value) {

            Log.d("QUEASCO", segundos.value.toString())
            Log.d("QUEASCO", minutos.value.toString())
            Log.d("QUEASCO", horas.value.toString())

            if (segundos.value == 0 && minutos.value >= 1) {
                minutos.value -= 1
                segundos.value += 60
            }

            if (minutos.value == 0 && horas.value >= 1) {
                horas.value -= 1
                minutos.value += 60
            }

            if (minutos.value == 0 && segundos.value == 0 && horas.value == 0) {

                Log.d("TIMERBOOL", "Notificando vibracion: " + checkedVibration.value.toString())
                Log.d("TIMERBOOL", "Notificando sonido: " + checkSound.value.toString())

                if (checkedVibration.value) {
                    if (vibratorManager.areAllPrimitivesSupported() && checkedVibration.value) {
                        vibratorManager.vibrate(
                            VibrationEffect.createWaveform(
                                timings,
                                amplitudes,
                                repeatIndex
                            )
                        )
                    } else {
                        vibratorManager.vibrate(
                            VibrationEffect.createOneShot(
                                1000,
                                50
                            )

                        )
                    }
                }

                if (checkSound.value) ringtone.play()

                if (!countdownRunning.value) {
                    if (checkSound.value) ringtone.stop()
                    if (checkedVibration.value) vibratorManager.cancel()

                    db.statisticDao().insert(
                        Statistic(
                            labelId = labelSelected.value.id,
                            dedicatedSeconds = dedicatedSecondsAccumulated.value,
                            timestamp = System.currentTimeMillis()
                        )
                    )

                }

            }

            if (!countdownRunning.value && worked.value) {

                db.statisticDao().insert(
                    Statistic(
                        labelId = labelSelected.value.id,
                        dedicatedSeconds = dedicatedSecondsAccumulated.value,
                        timestamp = System.currentTimeMillis()
                    )
                )
                worked.value = false
            }

            Log.d("TIMERBOOL", countdownRunning.value.toString())
            Log.d("TIMERBOOL", segundos.value.toString())

            if (countdownRunning.value && segundos.value > 0) {
                delay(1.seconds)
                segundos.value -= 1
                //labelSelected.value.dedicatedSeconds++
                dedicatedSecondsAccumulated.value++
                worked.value = true
            }

        }
    }

    if(orientation == Configuration.ORIENTATION_LANDSCAPE){

        LandscapeView(modeFlipStudy, countdownRunning, countdown, checkSound, checkedVibration, horas, minutos, segundos, openDialog, labelSelected, openModalBottomSheet )

    }else {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val options = listOf("Flip&Study", stringResource(R.string.timer_segmentedbutton))
            SegmentedButton(
                items = options,
                defaultSelectedItemIndex = if (modeFlipStudy.value) 0 else 1,
                cornerRadius = 50,
                color = MaterialTheme.colorScheme.primary,
                useFixedWidth = true,
                itemWidth = 150.dp
            ) {
                Log.e("CustomToggle", "Selected item : ${options[it]} $it")
                modeFlipStudy.value = it == 0
            }

            Card(
                modifier = Modifier
                    .shadow(elevation = 5.dp, shape = AbsoluteRoundedCornerShape(20.dp))
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(size = 12.dp)
                    )
                    .fillMaxWidth(),
                shape = AbsoluteRoundedCornerShape(20.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    Box(
                        modifier = Modifier
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null
                            ) { openModalBottomSheet.value = true }
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(9.dp, Alignment.Start),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 50.dp)
                        ) {
                            Icon(
                                modifier = Modifier.size(35.dp),
                                imageVector = Icons.Filled.Label,
                                contentDescription = "Etiqueta",
                                tint = colorEnumToColor(labelSelected.value.color)
                            )
                            Text(
                                text = labelSelected.value.name,
                                style = MaterialTheme.typography.displaySmall,
                                modifier = Modifier.padding(horizontal = 5.dp)
                            )
                        }
                    }

                    TextButton(
                        onClick = {
                            countdownRunning.value = false
                            intsToCountdown(horas, minutos, segundos, countdown)
                            openDialog.value = true
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {

                        Text(
                            text = horas.value.toString(),
                            fontSize = 80.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = ":",
                            fontSize = 80.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = minutos.value.toString(),
                            fontSize = 80.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = ":",
                            fontSize = 80.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = segundos.value.toString(),
                            fontSize = 80.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(
                            160.dp,
                            Alignment.CenterHorizontally
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(top = 30.dp)
                            .padding(horizontal = 20.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.Start),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                modifier = Modifier.size(35.dp),
                                imageVector = Icons.Filled.Vibration,
                                contentDescription = "Etiqueta",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                            Checkbox(
                                checked = checkedVibration.value,
                                onCheckedChange = {
                                    checkedVibration.value = !checkedVibration.value
                                }
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(45.dp)
                                    .padding(end = 10.dp),
                                imageVector = Icons.Filled.VolumeUp,
                                contentDescription = "Etiqueta",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                            Checkbox(
                                checked = checkSound.value,
                                onCheckedChange = { checkSound.value = !checkSound.value }
                            )
                        }
                    }

                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.Start),
                verticalAlignment = Alignment.Top,
                modifier = Modifier.padding(top = 50.dp)
            ) {

                if (modeFlipStudy.value.not()) {

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .size(99.dp)
                            .clickable { countdownRunning.value = !countdownRunning.value },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(35.dp),
                            imageVector = if (countdownRunning.value) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                            contentDescription = "Etiqueta",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                }

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .size(99.dp)
                        .clickable {
                            intsToCountdown(horas, minutos, segundos, countdown)
                            openDialog.value = true
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(35.dp),
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Etiqueta",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

            }
        }
    }
}

@Composable
fun LandscapeView(
    modeFlipStudy: MutableState<Boolean>,
    countdownRunning: MutableState<Boolean>,
    countdown: MutableState<String>,
    checkSound: MutableState<Boolean>,
    checkedVibration: MutableState<Boolean>,
    horas: MutableState<Int>,
    minutos: MutableState<Int>,
    segundos: MutableState<Int>,
    openDialog: MutableState<Boolean>,
    labelSelected: MutableState<Label>,
    openModalBottomSheet: MutableState<Boolean>
) {

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(start = 20.dp, top = 20.dp, bottom = 20.dp)
    ) {
        Card(
            modifier = Modifier
                .shadow(elevation = 5.dp, shape = AbsoluteRoundedCornerShape(20.dp))
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(size = 12.dp)
                )
                .fillMaxHeight(),
            shape = AbsoluteRoundedCornerShape(20.dp)
        ){
            Row(
                horizontalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    Box(
                        modifier = Modifier
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null
                            ) { openModalBottomSheet.value = true }
                            .align(Alignment.CenterHorizontally),
                        contentAlignment = Alignment.Center
                    ) {

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(9.dp, Alignment.Start),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 40.dp)
                        ) {
                            Icon(
                                modifier = Modifier.size(35.dp),
                                imageVector = Icons.Filled.Label,
                                contentDescription = "Etiqueta",
                                tint = colorEnumToColor(labelSelected.value.color)
                            )
                            Text(
                                text = labelSelected.value.name,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(horizontal = 5.dp)
                            )
                        }
                    }

                    TextButton(
                        onClick = {
                            countdownRunning.value = false
                            intsToCountdown(horas, minutos, segundos, countdown)
                            openDialog.value = true
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {

                        Text(
                            text = horas.value.toString(),
                            style = MaterialTheme.typography.displayLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = ":",
                            style = MaterialTheme.typography.displayLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = minutos.value.toString(),
                            style = MaterialTheme.typography.displayLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = ":",
                            style = MaterialTheme.typography.displayLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = segundos.value.toString(),
                            style = MaterialTheme.typography.displayLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(
                            160.dp,
                            Alignment.CenterHorizontally
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.Start),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                modifier = Modifier.size(35.dp),
                                imageVector = Icons.Filled.Vibration,
                                contentDescription = "Etiqueta",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                            Checkbox(
                                checked = checkedVibration.value,
                                onCheckedChange = {
                                    checkedVibration.value = !checkedVibration.value
                                }
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(45.dp)
                                    .padding(end = 10.dp),
                                imageVector = Icons.Filled.VolumeUp,
                                contentDescription = "Etiqueta",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                            Checkbox(
                                checked = checkSound.value,
                                onCheckedChange = { checkSound.value = !checkSound.value }
                            )
                        }
                    }


                }
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val options = listOf("Flip&Study", stringResource(R.string.timer_segmentedbutton))
            SegmentedButton(
                items = options,
                defaultSelectedItemIndex = if (modeFlipStudy.value) 0 else 1,
                cornerRadius = 50,
                color = MaterialTheme.colorScheme.primary,
                useFixedWidth = true,
                itemWidth = 150.dp
            ) {
                Log.e("CustomToggle", "Selected item : ${options[it]} $it")
                modeFlipStudy.value = it == 0
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.Start),
                verticalAlignment = Alignment.Top,
            ) {
                if (modeFlipStudy.value.not()) {

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .size(70.dp)
                            .clickable { countdownRunning.value = !countdownRunning.value },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(35.dp),
                            imageVector = if (countdownRunning.value) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                            contentDescription = "Etiqueta",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                }

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .size(70.dp)
                        .clickable {
                            intsToCountdown(horas, minutos, segundos, countdown)
                            openDialog.value = true
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(35.dp),
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Etiqueta",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }

}