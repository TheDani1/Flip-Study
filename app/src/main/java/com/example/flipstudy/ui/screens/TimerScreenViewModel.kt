package com.example.flipstudy.ui.screens

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flipstudy.ui.label.data.Label
import com.example.flipstudy.ui.label.data.LabelsRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.time.Duration

class TimerScreenViewModel(private val labelsRepository: LabelsRepository) : ViewModel(){

    var state by mutableStateOf(TimerScreenState())
        private set

    init {
        viewModelScope.launch {
            labelsRepository.getAllLabelsStream().collectLatest {
                state = state.copy(
                    labels = it
                )

            }
        }
    }

}