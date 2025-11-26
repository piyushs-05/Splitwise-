package com.example.splitwise_final

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SettleUpApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}

