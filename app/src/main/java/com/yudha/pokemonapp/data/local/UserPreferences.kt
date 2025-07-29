package com.yudha.pokemonapp.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    private val authTokenKey = stringPreferencesKey("auth_token")
    private val userIdKey = longPreferencesKey("user_id")

    val authToken: Flow<String?> = context.dataStore.data.map {
        it[authTokenKey]
    }
    
    val userId: Flow<Long> = context.dataStore.data.map {
        it[userIdKey] ?: -1L
    }

    suspend fun saveAuthToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[authTokenKey] = token
        }
    }
    
    suspend fun saveUserId(userId: Long) {
        context.dataStore.edit { preferences ->
            preferences[userIdKey] = userId
        }
    }
    
    suspend fun getUserId(): Long {
        return context.dataStore.data.map {
            it[userIdKey] ?: -1L
        }.first()
    }

    suspend fun clear() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}