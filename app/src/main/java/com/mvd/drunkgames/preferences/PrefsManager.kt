package com.mvd.drunkgames.preferences

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

@SuppressLint("StaticFieldLeak")
object PrefsManager {

    private val MICROPHONE = "microphone"
    private val ACCELEROMETR = "accelerometr"
    private val MODE = "game_mode"

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var context: Context

    fun init(application: Application) {
        context = application.baseContext
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun getMicrophoneSensitivity(): Float {
        return sharedPreferences.getInt(MICROPHONE, 1).toFloat()
    }

    fun getAccelerometrSensitivity(): Float {
        return sharedPreferences.getInt(ACCELEROMETR, 80).toFloat()
    }

    fun getGameMode(): Int {
        val mode = sharedPreferences.getString(MODE, "0")
        if (mode != null)
            return mode.toInt()
        return 0
    }

}