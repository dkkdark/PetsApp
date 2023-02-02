package com.kseniabl.petsapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PetsApplication: Application() {

    override fun onCreate() {
        super.onCreate()
    }
}