package com.example.petcare.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SchedulePreferences(private val dataStore: DataStore<Preferences>) {
    private val isDialogShow = booleanPreferencesKey("dialog")

    fun isDialogShow(): Flow<Boolean> = dataStore.data.map { it[isDialogShow] ?: false }
    suspend fun setDialogShown() {
        dataStore.edit {
            it[isDialogShow] = true
        }
    }
}