package com.uberalles.dicodingstorysubmission.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UserPrefs private constructor(private val dataStore: DataStore<Preferences>) {

    private val token = stringPreferencesKey("token")
    private val userId = stringPreferencesKey("UserId")
    private val name = stringPreferencesKey("name")

    fun getToken(): Flow<String> = dataStore.data.map { it[token] ?: preferenceDefaultValue }
    fun getUserId(): Flow<String> = dataStore.data.map { it[userId] ?: preferenceDefaultValue }
    fun getName(): Flow<String> = dataStore.data.map { it[name] ?: preferenceDefaultValue }

    suspend fun saveLoginSession(token: String, userId: String, name: String) {
        dataStore.edit { preferences ->
            preferences[this.token] = token
            preferences[this.userId] = userId
            preferences[this.name] = name
        }
    }

    suspend fun clearLoginSession() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPrefs? = null
        const val preferenceDefaultValue = "Null"
        fun getInstance(dataStore: DataStore<Preferences>): UserPrefs {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPrefs(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}