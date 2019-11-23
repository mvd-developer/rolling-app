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
        sharedPreferences.edit().putInt(MICROPHONE, boarder).apply()
    }

    fun getAccelerometrSensitivity(): Float {
        return sharedPreferences.getInt(ACCELEROMETR, 12).toFloat()
    }

    fun setAccelerometrSensitivity(sensitivity: Int) {
        sharedPreferences.edit().putInt(ACCELEROMETR, sensitivity).apply()
    }

    //0 - deathMode, 1 - countdownMode
    fun getGameMode(): Int {
        return sharedPreferences.getInt(MODE, 0)
    }

    //0 - deathMode, 1 - countdownMode
    fun setGameMode(mode: Int) {
        sharedPreferences.edit().putInt(MODE, mode).apply()
    }

    fun getUserId(): String? {
        return sharedPreferences.getString(MODE, "")
    }

    fun setUserId(userId: String) {
        sharedPreferences.edit().putString(MODE, userId).apply()
    }

}