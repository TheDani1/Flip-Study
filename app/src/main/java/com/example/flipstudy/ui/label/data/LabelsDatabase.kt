package com.example.flipstudy.ui.label.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Label::class], version = 1, exportSchema = false)
abstract class LabelsDatabase : RoomDatabase() {

    abstract fun labelDao(): LabelDao

    companion object {
        @Volatile
        private var Instance: LabelsDatabase? = null

        fun getDatabase(context: Context): LabelsDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, LabelsDatabase::class.java, "labels_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }



}