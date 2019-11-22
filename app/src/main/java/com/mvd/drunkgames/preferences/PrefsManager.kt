package com.mvd.drunkgames.preferences

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

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


    fun getVoiceDetectionLowBoarder(): Int {
        return sharedPreferences.getInt(MICROPHONE, 20000)
    }

    fun setVoiceDetectionLowBoarder(boarder: Int) {
        sharedPreferences.edit().putInt(MICROPHONE, boarder)
    }

    fun getAccelerometrSensitivity(): Float {
        return sharedPreferences.getFloat(ACCELEROMETR, 12f)
    }

    fun setAccelerometrSensitivity(sensitivity: Float) {
        sharedPreferences.edit().putFloat(ACCELEROMETR, sensitivity)
    }

    fun getGameMode(): Int {
        val mode = sharedPreferences.getString(MODE, "0")
        if (mode != null)
            return mode.toInt()
        return 0
    }

}