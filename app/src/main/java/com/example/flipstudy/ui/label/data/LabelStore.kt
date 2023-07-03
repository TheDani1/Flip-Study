package com.example.flipstudy.ui.label.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LabelStore(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("labels_list")
        private val LABEL = stringPreferencesKey("label")
    }

    fun getSavedLabel(): Label {
        val placeJson = sharedPreferences().getString("place","{}")
        return Gson().fromJson(placeJson, Label::class.java)
    }

    private fun sharedPreferences() = context.getSharedPreferences(
        "sunny_weather",
        Context.MODE_PRIVATE
    )


    private val gson: Gson = Gson()

    suspend fun saveData(name: List<Label>) {
        context.dataStore.edit { preferences ->
            val jsonString = gson.toJson(name)
            preferences[LABEL] = jsonString
        }
    }

    val getData: Flow<List<Label>?> = context.dataStore.data
        .map { preferences ->
            val jsonString = preferences[LABEL] ?: "{}"
            Log.d("QUEASCO", jsonString)
            gson.fromJson(jsonString, Array<Label>::class.java).toList()
        }
}