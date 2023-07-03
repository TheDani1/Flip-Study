package com.example.flipstudy.ui.screens

import android.media.Ringtone
import android.os.Build
import android.os.CombinedVibration
import android.os.VibrationEffect
import android.os.VibratorManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.RingVolume
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.compose.FlipStudyTheme
import com.example.flipstudy.R
import com.example.flipstudy.ui.components.CountdownActions
import com.example.flipstudy.ui.components.CountdownSetterDialog
import com.example.flipstudy.ui.components.SegmentedButton
import com.example.flipstudy.ui.label.data.ColorEnum
import com.example.flipstudy.ui.label.data.Label
import com.example.flipstudy.ui.label.data.LabelDatabase
import com.example.flipstudy.ui.label.data.colorEnumToColor
import com.example.flipstudy.ui.label.ui.ModalBottomSheet
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

    var horasString = ""
    var minutosString = ""
    var segundosString = ""

    copyh += copym / 60
    copym += copys / 60

    copym %= 60
    copys %= 60

    horasString = if (copyh < 9) {
        "0$copyh"
    } else {
        copyh.toString()
    }

    minutosString = if (copym < 9) {
        "0$copym"

    } else {
        copym.toString()
    }

    segundosString = if (copys < 9) {
        "0$copys"
    } else {
        copys.toString()
    }

    countdown.value = horasString + minutosString + segundosString
}

fun segundos(tiempo: MutableState<String>): String {
    if (tiempo.value.length >= 2) {
        return tiempo.value.takeLast(2)
    } else if (tiempo.value.length == 1) {
        return "0" + tiempo.value.takeLast(1)
    } else {
        return "00"
    }
}

fun minutos(tiempo: MutableState<String>): String {
    if (tiempo.value.length >= 4) {
        return tiempo.value.takeLast(4).take(2)
    } else if (tiempo.value.length == 3) {
        return "0" + tiempo.value.takeLast(3).take(1)
    } else {
        return "00"
    }
}

fun horas(tiempo: MutableState<String>): String {
    if (tiempo.value.length >= 6) {
        return tiempo.value.takeLast(6).take(2)
    } else if (tiempo.value.length == 5) {
        return "0" + tiempo.value.takeLast(5).take(1)
    } else {
        return "00"
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun TimerScreen(
    navController: NavController,
    db: LabelDatabase,
    sensorAvailability: MutableState<Boolean>,
    sensorValues: MutableState<Float>,
    vibratorManager: VibratorManager,
    ringtone: Ringtone,
) {

    var ticks = rememberSaveable { mutableStateOf(0) }

    var checkedState = rememberSaveable { mutableStateOf(true) }

    var checked = rememberSaveable { mutableStateOf(true) }
    val openDialog = rememberSaveable { mutableStateOf(false) }

    var countdown = rememberSaveable { mutableStateOf("000000") }

    var worked = rememberSaveable { mutableStateOf(false) }

    var horas = rememberSaveable { mutableStateOf(0) }
    var minutos = rememberSaveable { mutableStateOf(0) }
    var segundos = rememberSaveable { mutableStateOf(10) }

    // TODO
    val timings: LongArray = longArrayOf(50, 50, 50, 50, 50, 50, 50)
    val amplitudes: IntArray = intArrayOf(33, 0, 75, 0, 170, 0, 200)
    val repeatIndex = 1 // Do not repeat.

    // H -> 0
    // M -> 1
    // S -> 2
    val openModalBottomSheet = rememberSaveable { mutableStateOf(false) }

    val labelSelected =
        rememberSaveable { mutableStateOf(Label(1, "Unlabelled", ColorEnum.GRAY, 0)) }

    // COUNTDOWN SETTER DIALOG
    /*CountdownSetterDialog(
        openDialog, actions = CountdownActions,
        modifier = Modifier.padding(8.dp), countdown, horas, minutos, segundos
    )

    // MODAL DE ETIQUETAS
    ModalBottomSheet(openModalBottomSheet = openModalBottomSheet, db, labelSelected)

    // COUNTDOWN EN SI
    LaunchedEffect(key1 = sensorValues.value <= 20f, key2 = segundos.value) {

        Log.d("QUEASCO", segundos.value.toString())
        Log.d("QUEASCO", minutos.value.toString())
        Log.d("QUEASCO", horas.value.toString())

        if(segundos.value == 0 && minutos.value >= 1){
            minutos.value -= 1
            segundos.value += 60
        }

        if(minutos.value == 0 && horas.value >= 1){
            horas.value -= 1
            minutos.value += 60
        }

        if(minutos.value == 0 && segundos.value == 0 && horas.value == 0){

            Log.d("BOOLEAN", checkedState.value.toString())


            if(vibratorManager.defaultVibrator.areAllPrimitivesSupported() && checkedState.value){
                vibratorManager.vibrate(
                    CombinedVibration.createParallel(VibrationEffect.createOneShot(1000,50))
                )
            }else{
                vibratorManager.vibrate(
                    CombinedVibration.createParallel(VibrationEffect.createWaveform(timings, amplitudes, repeatIndex))
                )
            }

            // TODO
            //ringtone.play()

            if(sensorValues.value > 20f){
                //ringtone.stop()
                vibratorManager.cancel()

                db.labelDao().update(Label(labelSelected.value.id, labelSelected.value.name,labelSelected.value.color, labelSelected.value.dedicatedSeconds))
                Log.d("SUBIDOBD",labelSelected.value.id.toString() + labelSelected.value.name + labelSelected.value.color + labelSelected.value.dedicatedSeconds )
            }

        }

        if(sensorValues.value > 20f && worked.value){
            db.labelDao().update(Label(labelSelected.value.id, labelSelected.value.name,labelSelected.value.color, labelSelected.value.dedicatedSeconds))
            worked.value = false
        }

        if(sensorValues.value <= 20f && segundos.value > 0) {
            delay(1.seconds)
            segundos.value -= 1
            labelSelected.value.dedicatedSeconds++
            worked.value = true
        }

    }*/

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val genders = listOf("Male", "Female")
        SegmentedButton(
            items = genders,
            defaultSelectedItemIndex = 0,
        ) {
            Log.e("CustomToggle", "Selected item : ${genders[it]}")
        }

        Card(
            modifier = Modifier
                .shadow(elevation = 5.dp, shape = AbsoluteRoundedCornerShape(20.dp))
                .background(
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = RoundedCornerShape(size = 12.dp)
                )
                .fillMaxWidth(),
            shape = AbsoluteRoundedCornerShape(20.dp)
        ){
            Column(
                verticalArrangement = Arrangement.spacedBy(0.dp,Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
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

                Text(
                    text = "0:0:0",
                    fontSize = 120.sp,
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(190.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top=30.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.Start),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start=10.dp)
                    ) {
                        Icon(
                            modifier = Modifier.size(45.dp),
                            imageVector = Icons.Filled.Label,
                            contentDescription = "Etiqueta",
                            tint = colorEnumToColor(labelSelected.value.color)
                        )
                        Checkbox(
                            checked = checkedState.value,
                            onCheckedChange = { checkedState.value = !checkedState.value }
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end=10.dp)
                    ) {
                        Icon(
                            modifier = Modifier.size(35.dp),
                            imageVector = Icons.Filled.Label,
                            contentDescription = "Etiqueta",
                            tint = colorEnumToColor(labelSelected.value.color)
                        )
                        Checkbox(
                            checked = checkedState.value,
                            onCheckedChange = { checkedState.value = !checkedState.value }
                        )
                    }
                }

            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.Start),
            verticalAlignment = Alignment.Top,
            modifier = Modifier.padding(top=50.dp)
        ) {
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.clip(CircleShape).size(99.dp),
                colors = IconButtonDefaults.iconButtonColors(MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    modifier = Modifier.size(35.dp),
                    imageVector = Icons.Filled.Label,
                    contentDescription = "Etiqueta",
                    tint = colorEnumToColor(labelSelected.value.color)
                )
            }

            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    modifier = Modifier.size(35.dp),
                    imageVector = Icons.Filled.Label,
                    contentDescription = "Etiqueta",
                    tint = colorEnumToColor(labelSelected.value.color)
                )
            }

        }
    }





    /*Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp), horizontalAlignment = Alignment.End
    ) {
        val genders = listOf("Male", "Female")
        SegmentedButton(
            items = genders,
            defaultSelectedItemIndex = 0
        ) {
            Log.e("CustomToggle", "Selected item : ${genders[it]}")
        }

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
                .background(
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = RoundedCornerShape(size = 20.dp)
                ),
            shape = AbsoluteRoundedCornerShape(20.dp)
        ) {
            Box(Modifier.fillMaxSize()) {

                Column(
                    Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp)
                ) {

                    TextButton(
                        onClick = {
                            intsToCountdown(horas, minutos, segundos, countdown)
                            openDialog.value = true
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = horas.value.toString(),
                            style = MaterialTheme.typography.displayLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = ":",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                        Text(
                            text = minutos.value.toString(),
                            style = MaterialTheme.typography.displayLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = ":",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                        Text(
                            text = segundos.value.toString(),
                            style = MaterialTheme.typography.displayLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Divider()

                    Box(
                        modifier = Modifier
                            .clickable { openModalBottomSheet.value = true }
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row() {
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

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Icon(
                            imageVector = Icons.Filled.Vibration,
                            contentDescription = "Vibrar"
                        )
                        Text(
                            text = "Vibrar",
                            style = MaterialTheme.typography.titleLarge,
                        )

                        Checkbox(
                            checked = checkedState.value,
                            onCheckedChange = { checkedState.value = !checkedState.value }
                        )

                        Icon(
                            imageVector = Icons.Filled.VolumeUp,
                            contentDescription = "Sonido"
                        )
                        Text(
                            text = "Sonido",
                            style = MaterialTheme.typography.titleLarge,
                        )

                        Checkbox(
                            checked = checkedState.value,
                            onCheckedChange = { checkedState.value = !checkedState.value }
                        )
                    }

                }
            }


        }

    }*/
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun viewWithVariables(){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(20.dp)
    ) {
        val genders = listOf("Male", "Female")
        SegmentedButton(
            items = genders,
            defaultSelectedItemIndex = 0
        ) {
            Log.e("CustomToggle", "Selected item : ${genders[it]}")
        }

        Card(
            modifier = Modifier
                .shadow(elevation = 5.dp, shape = AbsoluteRoundedCornerShape(20.dp))
                .background(
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = RoundedCornerShape(size = 12.dp)
                )
                .fillMaxWidth()
                .height(415.dp),
            shape = AbsoluteRoundedCornerShape(20.dp)
        ){
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(9.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier.size(35.dp),
                        imageVector = Icons.Filled.Label,
                        contentDescription = "Etiqueta",
                        tint = /*colorEnumToColor(labelSelected.value.color)*/Color.Magenta
                    )
                    Text(
                        text = /*labelSelected.value.name*/"Unlabelled",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(horizontal = 5.dp)
                    )
                }

                Text(
                    text = "0:0:0",
                    fontSize = 120.sp,
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(190.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.Start),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            modifier = Modifier.size(35.dp),
                            imageVector = Icons.Filled.Label,
                            contentDescription = "Etiqueta",
                            tint = /*colorEnumToColor(labelSelected.value.color)*/Color.Magenta
                        )
                        Checkbox(
                            checked = /*checkedState.value*/true,
                            onCheckedChange = { /*checkedState.value = !checkedState.value*/ }
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            modifier = Modifier.size(35.dp),
                            imageVector = Icons.Filled.Label,
                            contentDescription = "Etiqueta",
                            tint = /*colorEnumToColor(labelSelected.value.color)*/Color.Magenta
                        )
                        Checkbox(
                            checked = /*checkedState.value*/true,
                            onCheckedChange = { /*checkedState.value = !checkedState.value*/ }
                        )
                    }
                }

            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.Start),
            verticalAlignment = Alignment.Top,
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    modifier = Modifier.size(35.dp),
                    imageVector = Icons.Filled.Label,
                    contentDescription = "Etiqueta",
                    tint = /*colorEnumToColor(labelSelected.value.color)*/Color.Magenta
                )
            }

            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    modifier = Modifier.size(35.dp),
                    imageVector = Icons.Filled.Label,
                    contentDescription = "Etiqueta",
                    tint = /*colorEnumToColor(labelSelected.value.color)*/Color.Magenta
                )
            }

        }
    }
}