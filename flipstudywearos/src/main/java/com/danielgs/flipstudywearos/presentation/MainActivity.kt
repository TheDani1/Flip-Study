/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.danielgs.flipstudywearos.presentation

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.ambient.AmbientModeSupport
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import com.danielgs.flipstudywearos.presentation.theme.FlipStudyTheme
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.delay
import java.nio.charset.StandardCharsets
import kotlin.time.Duration.Companion.seconds

/**
 * Clase principal del proyecto. Extiende de [AmbientModeSupport] porque tiene que activar
 * la sincronización con el teléfono. [DataClient], [MessageClient] y [CapabilityClient] para
 * la recepción y envío de mensajes con el dispositivo móvil.
 *
 */
class MainActivity : AppCompatActivity(),
    AmbientModeSupport.AmbientCallbackProvider,
    DataClient.OnDataChangedListener,
    MessageClient.OnMessageReceivedListener,
    CapabilityClient.OnCapabilityChangedListener {

    /**
     * Variable que almacena el contexto de la aplicación
     */
    private var activityContext: Context? = null

    /**
     * Variable que almacena el TAG con el que se reciben mensajes
     */
    private val TAG_MESSAGE_RECEIVED = "receive1"

    /**
     * Variable que almacena la ruta del payload
     */
    private val APP_OPEN_WEARABLE_PAYLOAD_PATH = "/APP_OPEN_WEARABLE_PAYLOAD"

    /**
     * Variable que almacena si hay un dispositivo móvil conectado
     */
    private var mobileDeviceConnected: Boolean = false

    /**
     * Variable que almacena el string del ACK
     */
    private val wearableAppCheckPayloadReturnACK = "AppOpenWearableACK"

    /**
     * Variable que almacena la ruta donde se almacenan los items recibidos
     */
    private val MESSAGE_ITEM_RECEIVED_PATH: String = "/message-item-received"

    /**
     * Mensaje de evento
     */
    private var messageEvent: MessageEvent? = null

    /**
     * URI de nodo de dispositivo
     */
    private var mobileNodeUri: String? = null

    /**
     * Controlador para realizar el pareado
     */
    private lateinit var ambientController: AmbientModeSupport.AmbientController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityContext = this

        // Enables Always-on
        ambientController = AmbientModeSupport.attach(this)

        try {
            Wearable.getDataClient(activityContext!!).addListener(this)
            Wearable.getMessageClient(activityContext!!).addListener(this)
            Wearable.getCapabilityClient(activityContext!!)
                .addListener(this, Uri.parse("wear://"), CapabilityClient.FILTER_REACHABLE)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                this.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(VIBRATOR_SERVICE) as Vibrator
        }



        Log.d("LAUNCHED", "Attacheado")

        setContent {
            FlipStudyTheme {

                WearApp(mobileDeviceConnected, vibrator)

            }
        }
    }

    override fun onPause() {
        super.onPause()
        try {
            Wearable.getDataClient(activityContext!!).removeListener(this)
            Wearable.getMessageClient(activityContext!!).removeListener(this)
            Wearable.getCapabilityClient(activityContext!!).removeListener(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            Wearable.getDataClient(activityContext!!).addListener(this)
            Wearable.getMessageClient(activityContext!!).addListener(this)
            Wearable.getCapabilityClient(activityContext!!)
                .addListener(this, Uri.parse("wear://"), CapabilityClient.FILTER_REACHABLE)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDataChanged(p0: DataEventBuffer) {
        TODO("Not yet implemented")
    }

    override fun onMessageReceived(p0: MessageEvent) {

        try {
            Log.d(TAG_MESSAGE_RECEIVED, "onMessageReceived event received")
            val s1 = String(p0.data, StandardCharsets.UTF_8)
            val messageEventPath: String = p0.path

            Log.d(
                TAG_MESSAGE_RECEIVED,
                "onMessageReceived() A message from watch was received:"
                        + p0.requestId
                        + " "
                        + messageEventPath
                        + " "
                        + s1
            )

            //Send back a message back to the source node
            //This acknowledges that the receiver activity is open
            if (messageEventPath.isNotEmpty() && messageEventPath == APP_OPEN_WEARABLE_PAYLOAD_PATH) {
                try {
                    // Get the node id of the node that created the data item from the host portion of
                    // the uri.
                    val nodeId: String = p0.sourceNodeId.toString()
                    // Set the data of the message to be the bytes of the Uri.
                    val returnPayloadAck = wearableAppCheckPayloadReturnACK
                    val payload: ByteArray = returnPayloadAck.toByteArray()

                    // Send the rpc
                    // Instantiates clients without member variables, as clients are inexpensive to
                    // create. (They are cached and shared between GoogleApi instances.)
                    val sendMessageTask =
                        Wearable.getMessageClient(activityContext!!)
                            .sendMessage(nodeId, APP_OPEN_WEARABLE_PAYLOAD_PATH, payload)

                    Log.d(
                        TAG_MESSAGE_RECEIVED,
                        "Acknowledgement message successfully with payload : $returnPayloadAck"
                    )

                    messageEvent = p0
                    mobileNodeUri = p0.sourceNodeId

                    sendMessageTask.addOnCompleteListener {
                        if (it.isSuccessful) {
                            Log.d(TAG_MESSAGE_RECEIVED, "Message sent successfully")

                            val sbTemp = StringBuilder()
                            sbTemp.append("\nMobile device connected.")
                            Log.d("receive1", " $sbTemp")

                            mobileDeviceConnected = true

                        } else {
                            Log.d(TAG_MESSAGE_RECEIVED, "Message failed.")
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }//emd of if
            else if (messageEventPath.isNotEmpty() && messageEventPath == MESSAGE_ITEM_RECEIVED_PATH) {
                try {
                    val sbTemp = StringBuilder()
                    sbTemp.append("\n")
                    sbTemp.append(s1)
                    sbTemp.append(" - (Received from mobile)")
                    Log.d("receive1", " $sbTemp")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCapabilityChanged(p0: CapabilityInfo) {
        TODO("Not yet implemented")
    }

    override fun getAmbientCallback(): AmbientModeSupport.AmbientCallback = MyAmbientCallback()

    private inner class MyAmbientCallback : AmbientModeSupport.AmbientCallback() {
        override fun onEnterAmbient(ambientDetails: Bundle) {
            super.onEnterAmbient(ambientDetails)
        }

        override fun onUpdateAmbient() {
            super.onUpdateAmbient()
        }

        override fun onExitAmbient() {
            super.onExitAmbient()
        }
    }
}


/**
 * Función [Composable] encargada del renderizado de la aplicación WearOS
 *
 * @property mobileDeviceConnected Booleano que indica si hay un dispositivo móvil desconectado
 */
@Composable
fun WearApp(mobileDeviceConnected: Boolean, vibratorManager: Vibrator) {

    val horas = rememberSaveable { mutableStateOf(17) }
    val minutos = rememberSaveable { mutableStateOf(59) }
    val segundos = rememberSaveable { mutableStateOf(59) }

    val countdownRunning = rememberSaveable { mutableStateOf(false) }

    val checkedVibration = rememberSaveable { mutableStateOf(true) }

    val timings: LongArray = longArrayOf(50, 50, 50, 50, 50, 50, 50)
    val amplitudes: IntArray = intArrayOf(33, 0, 75, 0, 170, 0, 200)
    val repeatIndex = 1 // Do not repeat.

    val worked = rememberSaveable { mutableStateOf(false) }

    val workingNow = rememberSaveable { mutableStateOf("Trabajar") }

    LaunchedEffect(key1 = countdownRunning.value, key2 = segundos.value) {

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

            if (!countdownRunning.value) {
                if (checkedVibration.value) vibratorManager.cancel()
            }

        }

        if (!countdownRunning.value && worked.value) {

            worked.value = false
        }

        Log.d("TIMERBOOL", countdownRunning.value.toString())
        Log.d("TIMERBOOL", segundos.value.toString())

        if (countdownRunning.value && segundos.value > 0) {
            delay(1.seconds)
            segundos.value -= 1
            //labelSelected.value.dedicatedSeconds++
            worked.value = true
        }

    }

    val isSquare = remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    /* If you have enough items in your list, use [ScalingLazyColumn] which is an optimized
     * version of LazyColumn for wear devices with some added features. For more information,
     * see d.android.com/wear/compose.
     */
    Scaffold(
        modifier = Modifier
            .onSizeChanged {
                isSquare.value = it.width == it.height
            }
            .fillMaxSize(),
        timeText = {
            TimeText(
                timeTextStyle = TextStyle(
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Bold
                )
            )
        },
        vignette = {
            Vignette(vignettePosition = VignettePosition.TopAndBottom)
        }
    ) {

        val focusManager = LocalFocusManager.current

        TimerCountDown(
            workingNow = workingNow,
            horas = horas,
            minutos = minutos,
            segundos = segundos,
            isPaused = !countdownRunning.value,
            onPause = { countdownRunning.value = false; focusManager.clearFocus() },
            onResume = { countdownRunning.value = true; focusManager.clearFocus() },
            countdownRunning
        ) { countdownRunning.value = false; horas.value = 0; minutos.value = 0; segundos.value = 0 }


    }

}

@Composable
private fun TimerCountDown(
    workingNow: MutableState<String>,
    horas: MutableState<Int>,
    minutos: MutableState<Int>,
    segundos: MutableState<Int>,
    isPaused: Boolean,
    onPause: () -> Unit,
    onResume: () -> Unit,
    countdownRunning: MutableState<Boolean>,
    onSkip: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.Center),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {

        // LABEL
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 40.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,

            ) {
            Icon(
                imageVector = Icons.Filled.Label,
                contentDescription = "Etiqueta",
                tint = MaterialTheme.colors.onBackground
            )

            BasicTextField(value = workingNow.value, onValueChange = { workingNow.value = it }, textStyle = TextStyle(fontSize = MaterialTheme.typography.title3.fontSize))
        }


        val horasS = rememberSaveable { mutableStateOf(horas.value.toString()) }
        val minutosS = rememberSaveable { mutableStateOf(minutos.value.toString()) }
        val segundosS = rememberSaveable { mutableStateOf(segundos.value.toString()) }

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            BasicTextField(
                modifier = Modifier.width(43.dp).padding(end = 5.dp).onFocusChanged {
                    if(it.isFocused){
                        countdownRunning.value = false
                    }},
                textStyle = TextStyle(
                    textAlign = TextAlign.Right,
                    fontSize = MaterialTheme.typography.display3.fontSize,
                    fontWeight = FontWeight.Bold
                ),
                value = "${horas.value}",
                onValueChange = { if (it != "") horas.value = it.toInt() },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            Text(
                modifier = Modifier.padding(end = 5.dp),
                text = ":",
                fontSize = MaterialTheme.typography.display3.fontSize,
                color = MaterialTheme.colors.primary
            )

            BasicTextField(
                modifier = Modifier.width(43.dp).onFocusChanged {
                    if(it.isFocused){
                        countdownRunning.value = false
                    }},
                textStyle = TextStyle(fontSize = MaterialTheme.typography.display3.fontSize, fontWeight = FontWeight.Bold),
                value = "${minutos.value}",
                onValueChange = { if (it != "") minutos.value = it.toInt() },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            Text(
                modifier = Modifier.padding(end = 5.dp),
                text = ":",
                fontSize = MaterialTheme.typography.display3.fontSize,
                color = MaterialTheme.colors.primary
            )

            BasicTextField(
                modifier = Modifier.width(43.dp).onFocusChanged {
                                                                if(it.isFocused){
                                                                    countdownRunning.value = false
                                                                }
                },
                textStyle = TextStyle(fontSize = MaterialTheme.typography.display3.fontSize, fontWeight = FontWeight.Bold),
                value = "${segundos.value}",
                onValueChange = { if (it != "") segundos.value = it.toInt() },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
        }



        // Action buttons (play & skip)
        TimerActionButtons(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(align = Alignment.Center),
            isPaused = isPaused,
            onPause = onPause,
            onResume = onResume,
            onSkip = onSkip
        )
    }
}

@Composable
private fun TimerActionButtons(
    modifier: Modifier,
    isPaused: Boolean,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onSkip: () -> Unit,
) {
    Row(
        modifier = modifier
    ) {
        if (!isPaused) {
            Button(
                onClick = onPause,
                modifier = Modifier
                    .size(46.dp)
                    .padding(start = 4.dp, end = 4.dp)
                    .aspectRatio(1f),
            ) {
                Icon(
                    Icons.Default.Pause,
                    contentDescription = "Pause"
                )
            }
        } else {
            Button(
                onClick = onResume,
                modifier = Modifier
                    .size(46.dp)
                    .padding(start = 4.dp, end = 4.dp)
                    .aspectRatio(1f),
            ) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = "Play"
                )
            }
        }

        Button(
            onClick = onSkip,
            modifier = Modifier
                .size(46.dp)
                .padding(start = 4.dp, end = 4.dp)
                .aspectRatio(1f),
            colors = ButtonDefaults.iconButtonColors(),
        ) {
            Icon(
                Icons.Default.SkipNext,
                contentDescription = "Skip"
            )
        }
    }
}

@Composable
@Preview
fun PreviewWatch(){
    Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
        BasicTextField(
            modifier = Modifier.width(45.dp),
            value = "59",
            textStyle = MaterialTheme.typography.display3,
            onValueChange = {  },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )

        Text(
            modifier = Modifier.padding(end = 5.dp),
            text = ":",
            fontSize = MaterialTheme.typography.display3.fontSize,
            color = MaterialTheme.colors.primary
        )

        BasicTextField(
            modifier = Modifier.width(45.dp),
            value = "59",
            textStyle = MaterialTheme.typography.display3,
            onValueChange = {  },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )

        Text(
            modifier = Modifier.padding(end = 5.dp),
            text = ":",
            fontSize = MaterialTheme.typography.display3.fontSize,
            color = MaterialTheme.colors.primary
        )

        BasicTextField(
            modifier = Modifier.width(45.dp),
            value = "59",
            onValueChange = {  },
            textStyle = MaterialTheme.typography.display3,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )
    }
}