package com.mvd.drunkgames

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.mvd.drunkgames.modules.GameEvents
import com.mvd.drunkgames.modules.ShakeModule
import com.mvd.drunkgames.modules.SoundModule
import com.mvd.drunkgames.modules.VoiceDetectModule

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var soundModule: SoundModule
    private lateinit var shakeModule: ShakeModule
    private lateinit var voiceDetectModule: VoiceDetectModule
    val errorMessage = MutableLiveData<String>()
    val currentEvent = MutableLiveData<GameEvents>()

    /** Here we can show progress bar or some kind of it
     *  handle exception or something else
     */
    fun prepareAllModules() {
        soundModule = SoundModule(getApplication()) {
            if (it.isNullOrEmpty()) {
                //sound module is initialized
                // go ahead


            } else {
                //show error message
                errorMessage.postValue(it)
            }
        }
        shakeModule = ShakeModule(getApplication()) {
            currentEvent.postValue(it)
        }
    }


    fun startGame() {
        soundModule.playText(R.string.game_started)
        shakeModule.subscribeUpdates()
    }


    /**
     * Here we have to unsubscribe from all the observable game modules
     */

    override fun onCleared() {
        shakeModule.unSubscribeUpdates()
        super.onCleared()

    }
}