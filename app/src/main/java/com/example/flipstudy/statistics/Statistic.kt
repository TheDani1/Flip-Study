package com.example.flipstudy.statistics

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import com.example.flipstudy.label.data.Label
import kotlinx.parcelize.Parcelize

/**
 * Clase de dato que define lo que es una [Statistic] o Estadística.
 *
 * @property id Identificador único e inequívoco
 * @property labelId El identificador de etiqueta a la que pertenece esa estadística.
 * @property dedicatedSeconds Segundos dedicados a la etiqueta.
 * @property timestamp Marca de tiempo
 */
@Parcelize
@Entity(tableName = "statistics",
    foreignKeys = [ForeignKey(
        entity = Label::class,
        childColumns = ["label_id"],
        parentColumns = ["id"],
        onDelete = CASCADE
    )])
data class Statistic (
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    @ColumnInfo(name = "label_id") var labelId: Int,
    @ColumnInfo(name = "seconds_dedicated") var dedicatedSeconds: Int,
    @ColumnInfo(name = "timestamp") var timestamp: Long,
): Parcelable