package com.example.wallgodds.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallgodds.models.MainScreenEvent
import com.example.wallgodds.pref.AppPreferenceDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val appPreferenceDataStore: AppPreferenceDataStore
) : ViewModel() {

    val likeWallpaperSnackBarEnabled = appPreferenceDataStore.likeWallpaperSnackBarEnabled

    fun onEvent(event: MainScreenEvent) {
        when (event) {
            is MainScreenEvent.DisableLikeWallpaperSnackBar -> {
                viewModelScope.launch {
                    appPreferenceDataStore.disableLikeWallpaperSnackBar(false)
                }
            }
        }
    }
}