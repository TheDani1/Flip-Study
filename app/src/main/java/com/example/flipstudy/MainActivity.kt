package com.example.flipstudy

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
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

/**
 * Clase principal de la aplicación
 *
 */
class MainActivity : ComponentActivity(), SensorEventListener {

    private val values = MutableLiveData<Float>()

    private val rotatationValues = MutableLiveData<Float>()

    private lateinit var mySensorManager: SensorManager

    @SuppressLint("ServiceCast")
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlipStudyTheme{

                val goalsPreferences = GoalsPreferences(LocalContext.current)

                mySensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
                val sensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
                if (sensor != null) {
                    mySensorManager.registerListener(this, sensor,
                        SensorManager.SENSOR_DELAY_NORMAL)
                    Log.i("LIGHTSENSOR", "Registerered for TYPE_LIGHT Sensor")
                } else {
                    Log.e("LIGHTSENSOR", "Registerered for TYPE_LIGHT Sensor")
                }

                val ringtoneManager = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
                val ringtone = RingtoneManager.getRingtone(applicationContext, ringtoneManager)

                val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val vibratorManager = this.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
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

                MainScreen(db, sensorValues, vibrator, ringtone, orientation, goalsPreferences, StatisticViewModel(db), rotationSensorValues)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        mySensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val sensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        val accelerometro = mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (sensor != null) {
            mySensorManager.registerListener(this, sensor,
                SensorManager.SENSOR_DELAY_NORMAL)
            Log.i("LIGHTSENSOR", "Registerered for TYPE_LIGHT Sensor")
        } else {
            Log.e("LIGHTSENSOR", "Registerered for TYPE_LIGHT Sensor")
        }

        if (accelerometro != null) {
            mySensorManager.registerListener(this, accelerometro,
                SensorManager.SENSOR_DELAY_NORMAL)
            Log.i("LIGHTSENSOR", "Registerered for TYPE_LIGHT Sensor")
        } else {
            Log.e("LIGHTSENSOR", "Registerered for TYPE_LIGHT Sensor")
        }
    }

    override fun onPause() {
        super.onPause()

        mySensorManager.unregisterListener(this)
        Log.d("LIGHTSENSOR", "Unregistered")

    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_LIGHT) {
            values.postValue(event.values[0])
            Log.d("LIGHTSENSOR", event.values[0].toString())
        }

        if(event.sensor.type == Sensor.TYPE_ACCELEROMETER){
            rotatationValues.postValue(event.values[2])
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d("LIGHTSENSOR", "Olala")
    }
}