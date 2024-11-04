package com.qosquo.wallet.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

object PreferencesDataStoreHelper {
    private const val PREFERENCES_DATA_STORE_NAME = "settings"

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_DATA_STORE_NAME)

    internal suspend fun saveLongValue(
        value: Long,
        key: Preferences.Key<Long>,
        context: Context
    ) {
        context.dataStore.edit { saveData ->
            saveData[key] = value
        }
    }

    internal fun getLongValueFlow(
        key: Preferences.Key<Long>,
        context: Context
    ): Flow<Long?> {
        return context.dataStore.data
            .catch { exception ->
                when (exception) {
                    is IOException -> emit(emptyPreferences())
                    else -> throw exception
                }
            }
            .map { readData ->
                readData[key]
            }
    }

    internal suspend fun removeLongValueWithSpecificKey(
        key: Preferences.Key<Long>,
        context: Context
    ) {
        context.dataStore.edit { it.remove(key) }
    }

    internal suspend fun removeAllValues(context: Context) {
        context.dataStore.edit { it.clear() }
    }
}