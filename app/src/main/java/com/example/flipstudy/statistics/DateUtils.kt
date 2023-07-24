package com.example.flipstudy.statistics

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * Funciones útiles que ayudan al manejo de fechas.
 *
 */
class DateUtils {

    companion object {

        fun fromSecondsToTime(totalSecs: Int): String {

            val hours = totalSecs / 3600;
            val minutes = (totalSecs % 3600) / 60;
            val seconds = totalSecs % 60;

            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }

        fun mismaSemana(timestamp1: Long, timestamp2: Long): Boolean {
            val calendar1 = Calendar.getInstance(Locale("es","ES"));
            calendar1.firstDayOfWeek = Calendar.MONDAY

            calendar1.timeInMillis = timestamp1
            val semana1 = calendar1.get(Calendar.WEEK_OF_YEAR)

            calendar1.timeInMillis = timestamp2
            val semana2 = calendar1.get(Calendar.WEEK_OF_YEAR)

            Log.d("STATISTICS", "¿ $semana1 == $semana2 ?")
            return semana1 == semana2
        }

        fun obtenerDiaDeLaSemana(timestamp: Long): Int {
            val calendar = Calendar.getInstance(Locale("es","ES"));
            calendar.firstDayOfWeek = Calendar.MONDAY
            calendar.timeInMillis = timestamp

            return calendar.get(Calendar.DAY_OF_WEEK)
        }


    }

}