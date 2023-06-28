package com.example.flipstudy.ui.components

sealed interface AddSub {
    data class Number(val number: Int): AddSub
}