package com.example.flipstudy.statistics

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface StatisticDao {

    @Query("SELECT * from statistics ORDER BY timestamp ASC")
    fun getAllStatistics(): List<Statistic>

    @Query("SELECT * from statistics WHERE id = :id")
    fun getStatistic(id: Int): Statistic

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(statistic: Statistic)

    @Update
    suspend fun update(statistic: Statistic)

    @Delete
    suspend fun delete(statistic: Statistic)
}