package com.example.flipstudy.label.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


/**
 * Clase de dato que especifica la clase de la etiqueta [Label]
 *
 * @property id Identificador único e inequívoco de la etiqueta.
 * @property name Nombre de la etiqueta.
 * @property color Color de la etiqueta.
 */
@Parcelize
@Entity(tableName = "labels")
data class Label(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "colour") val color: ColorEnum,
): Parcelable
