package com.example.flipstudy.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.flipstudy.R
import com.example.flipstudy.ui.components.AddSub
import com.example.flipstudy.ui.components.BottomNavigation
import com.example.flipstudy.ui.components.CountdownActions
import com.example.flipstudy.ui.components.CountdownSetterDialog
import com.example.flipstudy.ui.components.TimerAction
import com.example.flipstudy.ui.label.data.Label
import com.example.flipstudy.ui.label.ui.ModalBottomSheet
import com.example.flipstudy.ui.navigation.AppNavigation

fun segundos(tiempo: MutableState<String>): String {
    if(tiempo.value.length >= 2){
        return tiempo.value.takeLast(2)
    }else if(tiempo.value.length == 1){
        return "0" + tiempo.value.takeLast(1)
    }else{
        return "00"
    }
}

fun minutos(tiempo: MutableState<String>): String{
    if(tiempo.value.length >= 4){
        return tiempo.value.takeLast(4).take(2)
    }else if(tiempo.value.length == 3){
        return "0" + tiempo.value.takeLast(3).take(1)
    }else{
        return "00"
    }
}

fun horas(tiempo: MutableState<String>): String{
    if(tiempo.value.length >= 6){
        return tiempo.value.takeLast(6).take(2)
    }else if(tiempo.value.length == 5){
        return "0" + tiempo.value.takeLast(5).take(1)
    }else{
        return "00"
    }
}

@Composable
fun TimerScreen(navController: NavController) {

    var checkedState = rememberSaveable { mutableStateOf(true) }

    var checked = rememberSaveable { mutableStateOf(true) }
    val openDialog = rememberSaveable { mutableStateOf(false) }

    var countdown = rememberSaveable { mutableStateOf("000000") }
    // H -> 0
    // M -> 1
    // S -> 2

    val openModalBottomSheet = rememberSaveable { mutableStateOf(false) }

    // 10h 11m 10s 10 horas 11 minutos  10 segundos

    CountdownSetterDialog(openDialog, actions = CountdownActions,
        modifier = Modifier.padding(8.dp), countdown)
    
    ModalBottomSheet(openModalBottomSheet = openModalBottomSheet)

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
                            text = horas(countdown),
                            style = MaterialTheme.typography.displayLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = ":",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                        Text(
                            text = minutos(countdown),
                            style = MaterialTheme.typography.displayLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = ":",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                        Text(
                            text = segundos(countdown),
                            style = MaterialTheme.typography.displayLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Divider()

                    Row(
                        modifier = Modifier.padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surface)
                                .clickable { openModalBottomSheet.value = true },
                            contentAlignment = Alignment.Center
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
                            onCheckedChange = { checkedState.value = !checkedState.value }
                        )
                    }


                    Switch(
                        checked = checked.value,
                        onCheckedChange = { checked.value = !checked.value },
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

fun TimerBody(modifier: Modifier) {


}