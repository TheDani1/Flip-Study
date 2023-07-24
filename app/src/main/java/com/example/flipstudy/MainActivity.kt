package com.example.flipstudy

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.MutableLiveData
import com.example.flipstudy.label.data.LabelDatabase
import com.example.flipstudy.screens.MainScreen
import com.example.flipstudy.statistics.GoalsPreferences
import com.example.flipstudy.statistics.StatisticViewModel
import com.example.flipstudy.theme.FlipStudyTheme
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.nio.charset.StandardCharsets

class MainActivity : AppCompatActivity(), SensorEventListener, CoroutineScope by MainScope(),
    DataClient.OnDataChangedListener,
    MessageClient.OnMessageReceivedListener,
    CapabilityClient.OnCapabilityChangedListener {

    var activityContext: Context? = null
    private val wearableAppCheckPayload = "AppOpenWearable"
    private val wearableAppCheckPayloadReturnACK = "AppOpenWearableACK"
    private var wearableDeviceConnected: Boolean = false

    private var currentAckFromWearForAppOpenCheck: String? = null
    private val APP_OPEN_WEARABLE_PAYLOAD_PATH = "/APP_OPEN_WEARABLE_PAYLOAD"

    private val MESSAGE_ITEM_RECEIVED_PATH: String = "/message-item-received"

    private val TAG_GET_NODES: String = "getnodes1"
    private val TAG_MESSAGE_RECEIVED: String = "receive1"

    private var messageEvent: MessageEvent? = null
    private var wearableNodeUri: String? = null

    private val values = MutableLiveData<Float>()

    private val rotatationValues = MutableLiveData<Float>()

    private lateinit var mySensorManager: SensorManager

    fun onClickButtonWear() {

        Log.d("LAUNCHED", "Ejecutada")

        if (!wearableDeviceConnected) {

            Log.d("LAUNCHED", "No hay wear Os conectado por lo que iniciamos conexion")

            val tempAct: Activity = activityContext as MainActivity
            initialiseDevicePairing(tempAct)
        }

        /*if (wearableDeviceConnected) {

            Log.d("LAUNCHED", "Si hay wear Os mandamos mensaje")

            val nodeId: String = messageEvent?.sourceNodeId!!
            // Set the data of the message to be the bytes of the Uri.
            val payload: ByteArray =
                "hola".toByteArray()

            // Send the rpc
            // Instantiates clients without member variables, as clients are inexpensive to
            // create. (They are cached and shared between GoogleApi instances.)
            val sendMessageTask =
                Wearable.getMessageClient(activityContext!!)
                    .sendMessage(nodeId, MESSAGE_ITEM_RECEIVED_PATH, payload)

            sendMessageTask.addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("send1", "Message sent successfully")
                    val sbTemp = StringBuilder()
                    sbTemp.append("\n")
                    sbTemp.append("hola")
                    sbTemp.append(" (Sent to Wearable)")
                    Log.d("receive1", " $sbTemp")
                } else {
                    Log.d("send1", "Message failed.")
                }
            }
        } else {
            Toast.makeText(
                activityContext,
                "Message content is empty. Please enter some message and proceed",
                Toast.LENGTH_SHORT
            ).show()
        }*/
    }

    private fun getNodes(context: Context): BooleanArray {
        val nodeResults = HashSet<String>()
        val resBool = BooleanArray(2)
        resBool[0] = false //nodePresent
        resBool[1] = false //wearableReturnAckReceived
        val nodeListTask =
            Wearable.getNodeClient(context).connectedNodes
        try {
            // Block on a task and get the result synchronously (because this is on a background thread).
            val nodes =
                Tasks.await(
                    nodeListTask
                )
            Log.e(TAG_GET_NODES, "Task fetched nodes")
            for (node in nodes) {
                Log.e(TAG_GET_NODES, "inside loop")
                nodeResults.add(node.id)
                try {
                    val nodeId = node.id
                    // Set the data of the message to be the bytes of the Uri.
                    val payload: ByteArray = wearableAppCheckPayload.toByteArray()
                    // Send the rpc
                    // Instantiates clients without member variables, as clients are inexpensive to
                    // create. (They are cached and shared between GoogleApi instances.)
                    val sendMessageTask =
                        Wearable.getMessageClient(context)
                            .sendMessage(nodeId, APP_OPEN_WEARABLE_PAYLOAD_PATH, payload)
                    try {
                        // Block on a task and get the result synchronously (because this is on a background thread).
                        val result = Tasks.await(sendMessageTask)
                        Log.d(TAG_GET_NODES, "send message result : $result")
                        resBool[0] = true

                        //Wait for 700 ms/0.7 sec for the acknowledgement message
                        //Wait 1
                        if (currentAckFromWearForAppOpenCheck != wearableAppCheckPayloadReturnACK) {
                            Thread.sleep(100)
                            Log.d(TAG_GET_NODES, "ACK thread sleep 1")
                        }
                        if (currentAckFromWearForAppOpenCheck == wearableAppCheckPayloadReturnACK) {
                            resBool[1] = true
                            return resBool
                        }
                        //Wait 2
                        if (currentAckFromWearForAppOpenCheck != wearableAppCheckPayloadReturnACK) {
                            Thread.sleep(250)
                            Log.d(TAG_GET_NODES, "ACK thread sleep 2")
                        }
                        if (currentAckFromWearForAppOpenCheck == wearableAppCheckPayloadReturnACK) {
                            resBool[1] = true
                            return resBool
                        }
                        //Wait 3
                        if (currentAckFromWearForAppOpenCheck != wearableAppCheckPayloadReturnACK) {
                            Thread.sleep(350)
                            Log.d(TAG_GET_NODES, "ACK thread sleep 5")
                        }
                        if (currentAckFromWearForAppOpenCheck == wearableAppCheckPayloadReturnACK) {
                            resBool[1] = true
                            return resBool
                        }
                        resBool[1] = false
                        Log.d(
                            TAG_GET_NODES,
                            "ACK thread timeout, no message received from the wearable "
                        )
                    } catch (exception: Exception) {
                        exception.printStackTrace()
                    }
                } catch (e1: Exception) {
                    Log.d(TAG_GET_NODES, "send message exception")
                    e1.printStackTrace()
                }
            } //end of for loop
        } catch (exception: Exception) {
            Log.e(TAG_GET_NODES, "Task failed: $exception")
            exception.printStackTrace()
        }
        return resBool
    }

    private fun initialiseDevicePairing(tempAct: Activity) {
        //Coroutine
        launch(Dispatchers.Default) {
            var getNodesResBool: BooleanArray? = null

            try {
                getNodesResBool =
                    getNodes(tempAct.applicationContext)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            //UI Thread
            withContext(Dispatchers.Main) {
                if (getNodesResBool!![0]) {
                    //if message Acknowlegement Received
                    if (getNodesResBool[1]) {
                        Toast.makeText(
                            activityContext,
                            "Wearable device paired and app is open. Tap the \"Send Message to Wearable\" button to send the message to your wearable device.",
                            Toast.LENGTH_LONG
                        ).show()

                        wearableDeviceConnected = true
                    } else {
                        Toast.makeText(
                            activityContext,
                            "A wearable device is paired but the wearable app on your watch isn't open. Launch the wearable app and try again.",
                            Toast.LENGTH_LONG
                        ).show()
                        wearableDeviceConnected = false
                    }
                } else {
                    Toast.makeText(
                        activityContext,
                        "No wearable device paired. Pair a wearable device to your phone using the Wear OS app and try again.",
                        Toast.LENGTH_LONG
                    ).show()

                    wearableDeviceConnected = false
                }
            }
        }
    }

    @SuppressLint("ServiceCast")
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityContext = this
        wearableDeviceConnected = false

        try {
            Wearable.getDataClient(activityContext!!).addListener(this)
            Wearable.getMessageClient(activityContext!!).addListener(this)
            Wearable.getCapabilityClient(activityContext!!)
                .addListener(this, Uri.parse("wear://"), CapabilityClient.FILTER_REACHABLE)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        setContent {
            FlipStudyTheme {

                val goalsPreferences = GoalsPreferences(LocalContext.current)

                mySensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
                val sensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
                if (sensor != null) {
                    mySensorManager.registerListener(
                        this, sensor,
                        SensorManager.SENSOR_DELAY_NORMAL
                    )
                    Log.i("LIGHTSENSOR", "Registerered for TYPE_LIGHT Sensor")
                } else {
                    Log.e("LIGHTSENSOR", "Registerered for TYPE_LIGHT Sensor")
                }

                val ringtoneManager = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
                val ringtone = RingtoneManager.getRingtone(applicationContext, ringtoneManager)

                val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val vibratorManager =
                        this.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                    vibratorManager.defaultVibrator
                } else {
                    @Suppress("DEPRECATION")
                    getSystemService(VIBRATOR_SERVICE) as Vibrator
                }

                val sensorValues by values.observeAsState(initial = 0f)
                val rotationSensorValues by rotatationValues.observeAsState(initial = 0f)

                val db = LabelDatabase.getInstance(applicationContext)
                Log.d("QUEASCO", "Main")

                val orientation = LocalConfiguration.current.orientation

                MainScreen(
                    db,
                    sensorValues,
                    vibrator,
                    ringtone,
                    orientation,
                    goalsPreferences,
                    StatisticViewModel(db),
                    rotationSensorValues,
                    onClickButtonWear = { onClickButtonWear() }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()

        mySensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val sensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        val accelerometro = mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (sensor != null) {
            mySensorManager.registerListener(
                this, sensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
            Log.i("LIGHTSENSOR", "Registerered for TYPE_LIGHT Sensor")
        } else {
            Log.e("LIGHTSENSOR", "Registerered for TYPE_LIGHT Sensor")
        }

        if (accelerometro != null) {
            mySensorManager.registerListener(
                this, accelerometro,
                SensorManager.SENSOR_DELAY_NORMAL
            )
            Log.i("LIGHTSENSOR", "Registerered for TYPE_LIGHT Sensor")
        } else {
            Log.e("LIGHTSENSOR", "Registerered for TYPE_LIGHT Sensor")
        }
    }

    override fun onPause() {
        super.onPause()
        try {
            mySensorManager.unregisterListener(this)
            Wearable.getDataClient(activityContext!!).removeListener(this)
            Wearable.getMessageClient(activityContext!!).removeListener(this)
            Wearable.getCapabilityClient(activityContext!!).removeListener(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_LIGHT) {
            values.postValue(event.values[0])
            Log.d("LIGHTSENSOR", event.values[0].toString())
        }

        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            rotatationValues.postValue(event.values[2])
            Log.d("ACELEROMETRO", event.values[2].toString())
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d("LIGHTSENSOR", "Olala")
    }

    override fun onDataChanged(p0: DataEventBuffer) {
        TODO("Not yet implemented")
    }

    override fun onMessageReceived(p0: MessageEvent) {
        try {
            val s =
                String(p0.data, StandardCharsets.UTF_8)
            val messageEventPath: String = p0.path
            Log.d(
                TAG_MESSAGE_RECEIVED,
                "onMessageReceived() Received a message from watch:"
                        + p0.requestId
                        + " "
                        + messageEventPath
                        + " "
                        + s
            )
            if (messageEventPath == APP_OPEN_WEARABLE_PAYLOAD_PATH) {
                currentAckFromWearForAppOpenCheck = s
                Log.d(
                    TAG_MESSAGE_RECEIVED,
                    "Received acknowledgement message that app is open in wear"
                )

                val sbTemp = StringBuilder()
                sbTemp.append("\nWearable device connected.")
                Log.d("receive1", " $sbTemp")

                messageEvent = p0
                wearableNodeUri = p0.sourceNodeId
            } else if (messageEventPath.isNotEmpty() && messageEventPath == MESSAGE_ITEM_RECEIVED_PATH) {

                try {
                    val sbTemp = StringBuilder()
                    sbTemp.append("\n")
                    sbTemp.append(s)
                    sbTemp.append(" - (Received from wearable)")
                    Log.d("receive1", " $sbTemp")

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("receive1", "Handled")
        }
    }

    override fun onCapabilityChanged(p0: CapabilityInfo) {
        TODO("Not yet implemented")
    }
}