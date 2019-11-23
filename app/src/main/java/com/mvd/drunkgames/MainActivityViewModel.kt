package com.mvd.drunkgames

import android.app.Application
import android.os.Handler
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.firebase.firestore.FirebaseFirestore
import com.mvd.drunkgames.base.SingleLiveEvent
import com.mvd.drunkgames.entity.GameSession
import com.mvd.drunkgames.entity.User
import com.mvd.drunkgames.modules.GameEvents
import com.mvd.drunkgames.modules.ShakeModule
import com.mvd.drunkgames.modules.SoundModule
import com.mvd.drunkgames.modules.VoiceDetectModule
import java.util.*
import java.util.concurrent.TimeUnit


private const val CONST_TIME_BETWEEN_ROUNDS = 4000L
private const val CONST_TIME_TO_WIN = 2000L
private const val CONST_MIN_TIME_TO_WIN = 1000L
private const val CONST_DECREASE_TIME_TO_WIN_FOR_ONE_ROUND = 100L

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
    val timeToWinLiveData = MutableLiveData<Long>()

    private var hasEvent = false
    var isGameStarted = false
        private set

    private var numberOfRounds = 0
        private set(value) {
            numberOfRoundsLiveData.postValue(value)
        }
    val numberOfRoundsLiveData = MutableLiveData<Int>()

    private val currentEventObserver = Observer<GameEvents> { t ->
        if (t != null && !hasEvent) {
            userAction = t
            Log.e("_curEventObs", "currentRound Value = $currentRound userAction = $userAction")
            hasEvent = true
        }
    }
    private lateinit var shakeEventLiveData: LiveData<GameEvents>
    private lateinit var voiceEventLiveData: LiveData<GameEvents>
    private var userActionEventLiveData = MutableLiveData<GameEvents>()

    private var userId: String = ""
    private var currentUser: User? = null

    var db = FirebaseFirestore.getInstance()


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
        userActionEventLiveData.observeForever(currentEventObserver)

        numberOfRounds = 0
    }

    private fun postDelayed(delayMillis: Long, callback: () -> Unit) {
        delayHandler.postDelayed(callback, delayMillis)
    }


    private fun startNewGame() {
        timeToWin = CONST_TIME_TO_WIN
        Thread {
            while (isGameStarted) {
                playOneRound()
                TimeUnit.MILLISECONDS.sleep(timeBetweenRounds)
            }
        }.start()
    }

    private fun playOneRound() {

        userAction = GameEvents.PASS
        //store current value
        currentRound = getCurrentRound(Random().nextInt(9))
        if (currentRound == GameEvents.PASS && timeToWin > CONST_MIN_TIME_TO_WIN) {
            timeToWin -= CONST_DECREASE_TIME_TO_WIN_FOR_ONE_ROUND
        }
        //tell the user what to do
        playRoundSound(currentRound)
        //give him time to think about it
        timeToWinLiveData.postValue(timeToWin)
        hasEvent = false
        postDelayed(timeToWin) {
            validateResult()

        }
    }


    private fun validateResult() {
        Log.e("_validateResult", "currentRound Value = $currentRound userAction = $userAction")
        if (userAction != currentRound) {
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
            if (numberOfRounds > 0)
                updateCurrentUser(numberOfRounds)
        } else {
            numberOfRounds += 1
        }
    }


    fun setUserAction(userAction: GameEvents) {
        userActionEventLiveData.postValue(userAction)
    }

    fun finishGame() {
        soundModule.stopMusic()
    }

    fun onLoginComplete(id: String) {
        userId = id
        if (id.isNotEmpty())
            checkForEntityInDB(id)
    }

    private fun getCurrentRound(round: Int): GameEvents {
        return when (round) {
            0, 1 -> GameEvents.CLICK
            2, 3 -> GameEvents.PULL
            4, 5 -> GameEvents.SCREAM
            6, 7 -> GameEvents.SHAKE
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
        if (userActionEventLiveData.hasObservers()) {
            userActionEventLiveData.removeObserver(currentEventObserver)
        }
    }

    private fun checkForEntityInDB(id: String) {
        val docRef = db.collection("users").document(id)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    currentUser = document.toObject(User::class.java)
                } else {
                    val newUser = User()
                    newUser.id = userId
                    newUser.gameSessions = listOf(GameSession())
                    docRef.set(newUser)
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }
    }


    private fun updateCurrentUser(round: Int) {
        val modifyGameSessions = mutableListOf<GameSession>()
        val gameSession = GameSession()
        gameSession.date = System.currentTimeMillis()
        gameSession.rounds = round
        currentUser!!.gameSessions?.let { modifyGameSessions.addAll(it) }
        modifyGameSessions.add(gameSession)
        currentUser!!.gameSessions = modifyGameSessions

        db.collection("users")
            .document(currentUser!!.id)
            .set(currentUser!!)
            .addOnSuccessListener {
                // Успешная запись
            }
            .addOnFailureListener {
                // Произошла ошибка при записи
            }
    }
}
