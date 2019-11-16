package com.mvd.drunkgames

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.mvd.drunkgames.modules.SoundModule

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var soundModule: SoundModule
    val errorMessage = MutableLiveData<String>()

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
    }


    fun startGame() {
        soundModule.playText(R.string.game_started)
    }


    /**
     * Here we have to unsubscribe from all the observable game modules
     */

    override fun onCleared() {
        super.onCleared()

    }
}