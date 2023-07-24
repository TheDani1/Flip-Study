package com.example.flipstudy.label.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.flipstudy.statistics.Statistic
import com.example.flipstudy.statistics.StatisticDao


/**
 * Clase abstracta de la base de datos de las etiquetas
 *
 */
@Database(entities = [Label::class, Statistic::class], version = 2)
abstract class LabelDatabase : RoomDatabase() {

    companion object {
        private const val DATABASE_NAME = "label_database"
        private const val DATABASE_DIR = "database/labelbd.db" // Asset/database/you_name.db

        fun getInstance(context: Context): LabelDatabase {
            return Room
                .databaseBuilder(context, LabelDatabase::class.java, DATABASE_NAME)
                .createFromAsset(DATABASE_DIR)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    abstract fun labelDao(): LabelDao
    abstract fun statisticDao(): StatisticDao
}