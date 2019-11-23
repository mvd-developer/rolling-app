package com.mvd.drunkgames.modules

import android.app.Application
import android.media.MediaPlayer
import android.speech.tts.TextToSpeech
import androidx.annotation.StringRes
import com.mvd.drunkgames.R

/** This is a preview only.
 * Brian Holmes - MelodyLoops.com;
 * Buy track at https://www.melodyloops.com/tracks/15-seconds-of-fun/
 */

class SoundModule(private val application: Application, callback: (errorMessage: String?) -> Unit) {
    private var speaker: TextToSpeech?
    private var mp = MediaPlayer.create(application, R.raw.audio)

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

    fun playMusic() {
        mp.setVolume(0.1f, 0.1f)
        mp.isLooping = true
        mp.start()
    }


    fun stopMusic() {
        mp?.stop()
        mp = MediaPlayer.create(application, R.raw.audio)
    }

    fun onDestroy() {
        speaker?.shutdown()
        mp?.stop()
        mp?.release()
        mp = null
        speaker = null
    }

}