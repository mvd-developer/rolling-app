package com.mvd.drunkgames

import android.Manifest.permission
import android.animation.ValueAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.SystemClock
import android.view.Menu
import android.view.MenuItem
import android.view.animation.LinearInterpolator
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.airbnb.lottie.LottieAnimationView
import com.mvd.drunkgames.base.Chronometer
import com.mvd.drunkgames.base.DialogCallback
import com.mvd.drunkgames.base.showErrorMessage
import com.mvd.drunkgames.modules.GameEvents
import com.mvd.drunkgames.preferences.SettingsActivity


class MainActivity : AppCompatActivity(), DialogCallback {

    private val RECORD_REQUEST_CODE = 101
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var tvButtonText: TextView
    private lateinit var tvCurrentRound: TextView
    private lateinit var lottieBtnStart: LottieAnimationView
    private lateinit var seekBar: SeekBar
    private var numberOfRounds = 0

    private lateinit var progressBarCircle: ProgressBar
    private lateinit var chronometer: Chronometer

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        tvButtonText = findViewById(R.id.tv_btn_text)
        tvButtonText.setText(R.string.start_game)
        tvCurrentRound = findViewById(R.id.tv_current_round)
        tvCurrentRound.text = getString(R.string.current_round, numberOfRounds)

        chronometer = findViewById(R.id.chronometer)
        progressBarCircle = findViewById(R.id.time_controller)
        lottieBtnStart = findViewById(R.id.btn_start)
        seekBar = findViewById(R.id.pull_btn)

        if (ContextCompat.checkSelfPermission(this, permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission.RECORD_AUDIO), RECORD_REQUEST_CODE)
        }
        viewModel = ViewModelProviders.of(this)[MainActivityViewModel::class.java]
        viewModel.prepareAllModules()

        viewModel.errorMessage.observe(this, Observer<String> {
            if (!it.isNullOrEmpty()) {
                showErrorMessage(it)
            }
        })

        viewModel.userFailed.observe(this, Observer<Boolean> {
            if (it != null && it) {
                showUserFailedDialog()
            }
        })

        viewModel.numberOfRoundsLiveData.observe(this, Observer<Int> {
            if (it != null) {
                numberOfRounds = it
                tvCurrentRound.text = getString(R.string.current_round, numberOfRounds)
            }
        })

        viewModel.timeToWinLiveData.observe(this, Observer<Long> {
            if (it != null) {
                startProgressBar(it)
            }
        })

        lottieBtnStart.setOnClickListener {
            if (viewModel.isGameStarted) {
                viewModel.setUserAction(GameEvents.CLICK)
            } else {
                startTimer()
                tvButtonText.setText(R.string.click)
                viewModel.startGame()
            }
        }


        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            var value = 0

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                value = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (seekBar == null) return

                if (value > seekBar.max - 5) {
                    viewModel.setUserAction(GameEvents.PULL)
                }
                val anim = ValueAnimator.ofInt(value, 0)
                anim.duration = 500
                anim.addUpdateListener { animation ->
                    val animProgress = animation.animatedValue as Int
                    seekBar.progress = animProgress
                }
                anim.start()
                value = 0
            }
        })
    }

    private fun startTimer() {
        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.stop()
        chronometer.start()

    }


    private fun startProgressBar(value: Long) {
        val anim = ValueAnimator.ofInt(1, progressBarCircle.max)
        anim.duration = value
        anim.interpolator = LinearInterpolator()
        anim.addUpdateListener { animation ->
            val animProgress = animation.animatedValue as Int
            progressBarCircle.progress = animProgress
        }
        anim.start()
    }


    private fun showUserFailedDialog() {
        chronometer.stop()
        tvButtonText.setText(R.string.start_game)
        DialogFailed.getInstance(numberOfRounds)
                .show(supportFragmentManager, DialogFailed::class.java.simpleName)
    }


    override fun playAgain() {
        startTimer()
        tvButtonText.setText(R.string.click)
        viewModel.startGame()
    }

    override fun cancel() {
        //do nothing
        //sign out maybe?
    }


    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_settings, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                this.startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
