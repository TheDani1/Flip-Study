package com.example.flipstudy.label.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface LabelDao {

    @Query("SELECT * from labels ORDER BY name ASC")
    fun getAllLabels(): List<Label>

    @Query("SELECT * from labels WHERE id = :id")
    fun getLabel(id: Int): Label

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(Label: Label)

    @Update
    suspend fun update(label: Label)

    @Delete
    suspend fun delete(label: Label)
}