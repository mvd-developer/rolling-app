package com.mvd.drunkgames

import android.app.Application
import android.os.Handler
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.mvd.drunkgames.base.SingleLiveEvent
import com.mvd.drunkgames.modules.GameEvents
import com.mvd.drunkgames.modules.ShakeModule
import com.mvd.drunkgames.modules.SoundModule
import com.mvd.drunkgames.modules.VoiceDetectModule
import java.util.*
import java.util.concurrent.TimeUnit

private const val CONST_TIME_BETWEEN_ROUNDS = 4000L

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var soundModule: SoundModule
    private lateinit var shakeModule: ShakeModule
    private lateinit var voiceDetectModule: VoiceDetectModule
    val errorMessage = MutableLiveData<String>()
    val userFailed = SingleLiveEvent<Boolean>()
    private val delayHandler = Handler()
    private var currentRound = GameEvents.PASS
    private var userAction = GameEvents.PASS
    private var timeBetweenRounds = 4000L
    private var timeToWin = 2000L
    var isGameStarted = false
        private set

    var numberOfRoundsLiveData = MutableLiveData<Int>()
        private set

    private val currentEventObserver = Observer<GameEvents> { t ->
        if (t != null) {
            userAction = t
        }
    }
    private lateinit var shakeEventLiveData: LiveData<GameEvents>
    private lateinit var voiceEventLiveData: LiveData<GameEvents>


    /** Here we can show progress bar or some kind of it
     *  handle exception or something else
     */
    fun prepareAllModules() {
        soundModule = SoundModule(getApplication()) {
            if (it.isNullOrEmpty()) {
                //sound module is initialized
                // go ahead
                shakeModule = ShakeModule(getApplication())
                voiceDetectModule = VoiceDetectModule(getApplication())
            } else {
                //show error message
                errorMessage.postValue(it)
            }
        }
    }


    fun startGame() {
        isGameStarted = true
        soundModule.playText(R.string.game_started)
        postDelayed(1500) {
            soundModule.playMusic()
            startNewGame()
        }
        shakeEventLiveData = shakeModule.subscribeUpdates()
        shakeEventLiveData.observeForever(currentEventObserver)
        voiceEventLiveData = voiceDetectModule.subscribeUpdates()
        voiceEventLiveData.observeForever(currentEventObserver)
    }

    private fun postDelayed(delayMillis: Long, callback: () -> Unit) {
        delayHandler.postDelayed(callback, delayMillis)
    }


    private fun startNewGame() {
        timeBetweenRounds = CONST_TIME_BETWEEN_ROUNDS
        Thread {
            while (isGameStarted) {
                playOneRound()
                TimeUnit.MILLISECONDS.sleep(timeBetweenRounds)
                timeBetweenRounds -= 100
            }
        }.start()
    }

    private fun playOneRound() {
        userAction = GameEvents.PASS
        //store current value
        currentRound = getCurrentRound(Random().nextInt(5))
        //tell the user what to do
        playRoundSound(currentRound)
        //give him time to think about it
        postDelayed(timeToWin) {
            validateResult()
        }
    }


    private fun validateResult() {
        if (userAction != currentRound) {
            Log.e("_userAction", "currentRound Value = $currentRound userAction = $userAction")
            isGameStarted = false
            soundModule.stopMusic()

            voiceDetectModule.unSubscribeUpdates()
            shakeModule.unSubscribeUpdates()
            if (voiceEventLiveData.hasObservers()) {
                voiceEventLiveData.removeObserver(currentEventObserver)
            }
            if (shakeEventLiveData.hasObservers()) {
                shakeEventLiveData.removeObserver(currentEventObserver)
            }
            userFailed.postValue(true)
        }
    }


    fun setUserAction(userAction: GameEvents) {
        this.userAction = userAction
    }

    fun finishGame() {
        soundModule.stopMusic()
    }


    private fun getCurrentRound(round: Int): GameEvents {
        return when (round) {
            0 -> GameEvents.CLICK
            1 -> GameEvents.PULL
            2 -> GameEvents.SCREAM
            3 -> GameEvents.SHAKE
            else -> GameEvents.PASS
        }
    }

    private fun playRoundSound(event: GameEvents) {
        val roundSound = when (event) {
            GameEvents.CLICK -> R.string.click
            GameEvents.PULL -> R.string.pull
            GameEvents.SCREAM -> R.string.scream
            GameEvents.SHAKE -> R.string.shake
            else -> R.string.pass
        }
        soundModule.playText(roundSound)
    }

    /**
     * Here we have to unsubscribe from all the observable game modules
     */

    override fun onCleared() {
        shakeModule.unSubscribeUpdates()
        voiceDetectModule.unSubscribeUpdates()
        super.onCleared()
        soundModule.onDestroy()

        if (shakeEventLiveData.hasObservers()) {
            shakeEventLiveData.removeObserver(currentEventObserver)
        }
        if (voiceEventLiveData.hasObservers()) {
            voiceEventLiveData.removeObserver(currentEventObserver)
        }
    }
}