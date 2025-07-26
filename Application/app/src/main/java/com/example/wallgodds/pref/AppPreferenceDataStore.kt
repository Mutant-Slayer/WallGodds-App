package com.example.wallgodds.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.appDataStore: DataStore<Preferences> by preferencesDataStore(name = "app_data_store")

@Singleton
class AppPreferenceDataStore
@Inject
constructor(
    @ApplicationContext private val context: Context,
) {
    companion object {
        private val LIKE_WALLPAPER_SNACK_BAR = booleanPreferencesKey("like_wallpaper_snack_bar")
    }

    val likeWallpaperSnackBarEnabled: Flow<Boolean> = context.appDataStore.data.map { preferences ->
        preferences[LIKE_WALLPAPER_SNACK_BAR] != false
    }

    suspend fun disableLikeWallpaperSnackBar(enabled: Boolean) {
        context.appDataStore.edit { prefs ->
            prefs[LIKE_WALLPAPER_SNACK_BAR] = enabled
        }
    }
}