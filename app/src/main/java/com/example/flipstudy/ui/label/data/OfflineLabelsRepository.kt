package com.example.flipstudy.ui.label.data

import kotlinx.coroutines.flow.Flow

class OfflineLabelsRepository(private val labelDao: LabelDao) : LabelsRepository {

    override fun getAllLabelsStream(): Flow<List<Label>> = labelDao.getAllLabels()

    override fun getLabelStream(id: Int): Flow<Label?> = labelDao.getLabel(id)

    override suspend fun insertLabel(label: Label) = labelDao.insert(label)

    override suspend fun deleteLabel(label: Label) = labelDao.delete(label)

    override suspend fun updateLabel(label: Label) = labelDao.update(label)

}