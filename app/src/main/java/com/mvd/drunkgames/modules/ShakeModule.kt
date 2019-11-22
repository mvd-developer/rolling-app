package com.mvd.drunkgames.modules

import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.mvd.drunkgames.preferences.PrefsManager
import kotlin.math.pow
import kotlin.math.sqrt


class ShakeModule(application: Application) :
    GameController(application), SensorEventListener {

    private val MIN_TIME_BETWEEN_SHAKES_MILLISECS = 1000
    private var SHAKE_THRESHOLD = 80f

    private var sensorManager: SensorManager
    private var accelerometer: Sensor

    private val liveData = MutableLiveData<GameEvents>()
    private var mLastShakeTime: Long = 0

    init {
        sensorManager = application.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        SHAKE_THRESHOLD = PrefsManager.getAccelerometrSensitivity()
    }

    override fun subscribeUpdates(): LiveData<GameEvents> {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)
        return liveData
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                val curTime = System.currentTimeMillis()
                if (curTime - mLastShakeTime > MIN_TIME_BETWEEN_SHAKES_MILLISECS) {

                    val x = event.values[0]
                    val y = event.values[1]
                    val z = event.values[2]

                    val acceleration = (sqrt(
                        x.toDouble().pow(2.0) +
                                y.toDouble().pow(2.0) +
                                z.toDouble().pow(2.0)
                    ) - SensorManager.GRAVITY_EARTH)

                    if (acceleration > SHAKE_THRESHOLD) {
                        mLastShakeTime = curTime
                        liveData.postValue(GameEvents.SHAKE)
                    }
                }
            }
        }
    }

    override fun unSubscribeUpdates() {
        sensorManager.unregisterListener(this)
    }

}