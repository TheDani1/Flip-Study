package com.example.flipstudy.ui.label.data

import kotlinx.coroutines.flow.Flow

interface LabelsRepository {

    fun getAllLabelsStream(): Flow<List<Label>>

    fun getLabelStream(id: Int): Flow<Label?>

    suspend fun insertLabel(label: Label)

    suspend fun deleteLabel(label: Label)

    suspend fun updateLabel(label: Label)
}