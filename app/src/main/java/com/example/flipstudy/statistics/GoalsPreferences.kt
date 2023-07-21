package com.example.flipstudy.statistics

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

public class GoalsPreferences(context: Context) {

    val MY_PREF = "MyPreferences"

    private val sharedPreferences : SharedPreferences = context.getSharedPreferences(MY_PREF, 0)
    private val editor : Editor = sharedPreferences.edit()

    fun set(key : String, value : Int){
        editor.putInt(key, value)
        editor.commit()
    }

    fun set(key: String, value : Boolean){
        editor.putBoolean(key, value)
        editor.commit()
    }

    fun get(key: String, defaultValue : Int) : Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    fun get(key: String, defaultValue : Boolean) : Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    fun clear(key: String){
        editor.remove(key)
        editor.commit()
    }

    fun clear(){
        editor.clear()
        editor.commit()
    }
}