package com.mvd.drunkgames.modules

import android.app.Application
import androidx.lifecycle.LiveData


/**
 * Each game controller should extend such base class
 */
abstract class GameController(application: Application) {

    abstract fun subscribeUpdates(): LiveData<GameEvents>

    abstract fun unSubscribeUpdates()

}