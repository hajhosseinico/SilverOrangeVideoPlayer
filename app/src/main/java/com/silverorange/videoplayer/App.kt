package com.silverorange.videoplayer

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// we need App class to initialize Hilt
@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}