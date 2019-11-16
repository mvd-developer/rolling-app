package com.mvd.drunkgames.modules

import android.app.Application
import android.speech.tts.TextToSpeech
import androidx.annotation.StringRes

class SoundModule(private val application: Application, callback: (errorMessage: String?) -> Unit) {
    private var speaker: TextToSpeech?

    init {
        speaker = TextToSpeech(application) {
            if (it == TextToSpeech.SUCCESS) {
                callback.invoke(null)
                configureSpeakerEngine()
            } else {
                speaker = null
                callback.invoke("Failed initialization of Speech engine")
            }
        }
    }


    private fun configureSpeakerEngine() {
        speaker!!.setPitch(1.0f)
        speaker!!.setSpeechRate(2.0f)
    }


    fun playText(@StringRes resId: Int) {
        speaker?.speak(application.getText(resId), TextToSpeech.QUEUE_FLUSH, null, "12")
    }


    fun onDestroy() {
        speaker?.shutdown()
    }

}