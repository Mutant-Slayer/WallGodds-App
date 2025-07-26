package com.example.wallgodds.models

sealed interface MainScreenEvent {
    data object DisableLikeWallpaperSnackBar : MainScreenEvent
}