package com.example.flipstudy.ui.label.data

import android.content.Context

interface AppContainer {
    val labelsRepository: LabelsRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineLabelsRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [LabelsRepository]
     */
    override val labelsRepository: LabelsRepository by lazy {
        OfflineLabelsRepository(LabelsDatabase.getDatabase(context).labelDao())
    }
}