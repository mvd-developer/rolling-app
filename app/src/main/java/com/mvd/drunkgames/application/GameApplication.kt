package com.mvd.drunkgames.application

import android.app.Application
import com.mvd.drunkgames.preferences.PrefsManager

class GameApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        val prefsManager = PrefsManager
        prefsManager.init(this)
    }
}