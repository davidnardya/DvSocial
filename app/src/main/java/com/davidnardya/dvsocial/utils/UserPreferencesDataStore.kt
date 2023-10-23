package com.davidnardya.dvsocial.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UserPreferencesDataStore @Inject constructor(private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user")

    suspend fun savePreferencesDataStoreValues(
        key: String,
        updatedValues: Any
    ) {
        when (updatedValues) {
            is Boolean -> {
                val dataStoreKey = booleanPreferencesKey(key)
                context.dataStore.edit { score ->
                    score[dataStoreKey] = updatedValues
                }
            }
            is Float -> {
                val dataStoreKey = floatPreferencesKey(key)
                context.dataStore.edit { score ->
                    score[dataStoreKey] = updatedValues
                }
            }
            is Long -> {
                val dataStoreKey = longPreferencesKey(key)
                context.dataStore.edit { score ->
                    score[dataStoreKey] = updatedValues
                }
            }
            is String -> {
                val dataStoreKey = stringPreferencesKey(key)
                context.dataStore.edit { score ->
                    score[dataStoreKey] = updatedValues
                }
            }
            is Int -> {
                val dataStoreKey = intPreferencesKey(key)
                context.dataStore.edit { score ->
                    score[dataStoreKey] = updatedValues
                }
            }
        }
    }

    suspend fun getPreferencesDataStoreValues(
        key: String,
        defaultValue: Any
    ): Any? {
        when (defaultValue) {
            is Boolean -> {
                val dataStoreKey = booleanPreferencesKey(key)
                val preferences = context.dataStore.data.first()
                return preferences[dataStoreKey]
            }
            is Float -> {
                val dataStoreKey = floatPreferencesKey(key)
                val preferences = context.dataStore.data.first()
                return preferences[dataStoreKey]
            }
            is Long -> {
                val dataStoreKey = longPreferencesKey(key)
                val preferences = context.dataStore.data.first()
                return preferences[dataStoreKey]
            }
            is String -> {
                val dataStoreKey = stringPreferencesKey(key)
                val preferences = context.dataStore.data.first()
                return preferences[dataStoreKey]
            }
            is Int -> {
                val dataStoreKey = intPreferencesKey(key)
                val preferences = context.dataStore.data.first()
                return preferences[dataStoreKey]
            }
            else -> {
                return null
            }
        }
    }

    suspend fun removeKey(key: String) {
        context.dataStore.edit {
            it.remove(stringPreferencesKey(key))
        }
    }

    suspend fun clear() {
        context.dataStore.edit {
            it.clear()
        }
    }
}