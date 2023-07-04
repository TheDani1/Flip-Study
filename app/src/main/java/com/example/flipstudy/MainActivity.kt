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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.flipstudy.ui.label.data.LabelDatabase
import com.example.flipstudy.ui.screens.MainScreen
import com.example.flipstudy.ui.theme.FlipStudyTheme


class MainActivity : ComponentActivity() {

    @SuppressLint("ServiceCast")
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlipStudyTheme{

                val mySensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

                val sensorAvailability =  remember { mutableStateOf(false) }

                val sensorValues = remember { mutableStateOf(0f) }

                val lightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

                val lightSensorListener: SensorEventListener = object : SensorEventListener {
                    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                        // TODO Auto-generated method stub
                    }

                    override fun onSensorChanged(event: SensorEvent) {
                        if (event.sensor.type == Sensor.TYPE_LIGHT) {
                            //textLIGHT_reading.setText("LIGHT: " + event.values[0])
                            sensorValues.value = event.values[0]
                            Log.d("LIGHTSENSOR", event.values[0].toString())
                        }
                    }
                }

                if(lightSensor != null){
                    sensorAvailability.value = true
                    mySensorManager.registerListener(
                        lightSensorListener,
                        lightSensor,
                        SensorManager.SENSOR_DELAY_NORMAL)

                } else {
                    sensorAvailability.value = false
                }

                //val vibratorManager : VibratorManager = getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager

                val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val vibratorManager = this.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                    vibratorManager.defaultVibrator
                } else {
                    @Suppress("DEPRECATION")
                    getSystemService(VIBRATOR_SERVICE) as Vibrator
                }



                val ringtoneManager = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
                val ringtone = RingtoneManager.getRingtone(applicationContext, ringtoneManager)

                val db = LabelDatabase.getInstance(applicationContext)
                Log.d("QUEASCO", "Main")


                MainScreen(db, sensorAvailability , sensorValues, vibrator, ringtone)
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }
}