package com.example.flipstudy.ui.label.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TimerDuracion(var horas: String, var minutos: String, var segundos: String) :
    Parcelable {
    fun minusSecond(){

        val horasL: Long = horas.toLong()
        val minutosL: Long = minutos.toLong()
        val segundosL: Long = segundos.toLong()


        if(segundosL > 0){
            segundosL.minus(1)
        }else if(minutosL > 0){
            minutosL.minus(1)
            segundosL.plus(60)
        }else if(horasL > 0){
            horasL.minus(1)
            minutosL.plus(60)
        }

        horas = horasL.toString()
        minutos = minutosL.toString()
        segundos = segundosL.toString()
    }
}
