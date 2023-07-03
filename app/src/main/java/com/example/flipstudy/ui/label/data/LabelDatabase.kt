package com.example.flipstudy.ui.label.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Label::class], version = 1)
abstract class LabelDatabase : RoomDatabase() {

    companion object {
        private const val DATABASE_NAME = "label_database"
        private const val DATABASE_DIR = "database/labelbd.db" // Asset/database/you_name.db

        fun getInstance(context: Context): LabelDatabase {
            return Room
                .databaseBuilder(context, LabelDatabase::class.java, DATABASE_NAME)
                .createFromAsset(DATABASE_DIR)
                .allowMainThreadQueries()
                .build()
        }
    }

    abstract fun labelDao(): LabelDao
}