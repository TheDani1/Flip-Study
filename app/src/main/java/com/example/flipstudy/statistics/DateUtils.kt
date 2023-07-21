package com.example.flipstudy.statistics

import java.util.concurrent.TimeUnit

class DateUtils {

    companion object {

        fun fromSecondsToTime(totalSecs: Int): String {

            val hours = totalSecs / 3600;
            val minutes = (totalSecs % 3600) / 60;
            val seconds = totalSecs % 60;

            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }

    }

}