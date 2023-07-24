package com.example.flipstudy.label.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

/**
 * DAO de etiqueta
 *
 */
@Dao
interface LabelDao {

    /**
     * Función para obtener todas las etiquetas
     *
     */
    @Query("SELECT * from labels ORDER BY name ASC")
    fun getAllLabels(): List<Label>

    /**
     * Función para obtener una etiqueta de una id
     *
     */
    @Query("SELECT * from labels WHERE id = :id")
    fun getLabel(id: Int): Label

    /**
     * Función para insertar una etiqueta
     *
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(Label: Label)

    /**
     * Función para actualizar una etiqueta
     *
     */
    @Update
    suspend fun update(label: Label)

    /**
     * Función para eliminar una etiqueta
     *
     */
    @Delete
    suspend fun delete(label: Label)
}