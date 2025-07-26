package com.example.wallgodds.di

import android.app.Application
import com.example.wallgodds.pref.AppPreferenceDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun getAppPreferenceDataStore(application: Application): AppPreferenceDataStore {
        return AppPreferenceDataStore(application)
    }
}