package com.mvd.drunkgames

import android.Manifest
import android.Manifest.permission.RECORD_AUDIO
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mvd.drunkgames.modules.GameEvents
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private val RECORD_REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        ///////////////////
        viewModel.maxVolume.observe(this, Observer<String> {
            findViewById<TextView>(R.id.maxVol).text = it
            start_btn.text = applicationContext.getString(R.string.start_game)
            findViewById<SeekBar>(R.id.seekBar).setEnabled(true);
        })

        findViewById<TextView>(R.id.seekBarVal).text =
            (findViewById<SeekBar>(R.id.seekBar).progress).toString()
        /////////////////////////


        findViewById<Button>(R.id.start_btn).setOnClickListener {
            //  viewModel.startGame()


            /////////////////////////////
            var text = "";
            if (start_btn.text.equals(applicationContext.getString(R.string.stop_game))) {
                text = applicationContext.getString(R.string.start_game)
                findViewById<SeekBar>(R.id.seekBar).setEnabled(true);
                viewModel.unSubscribeUpdatesVoiceDetectModule()
            }

            if (start_btn.text.equals(applicationContext.getString(R.string.start_game))) {
                text = applicationContext.getString(R.string.stop_game)
                findViewById<TextView>(R.id.maxVol).text = "max volume"
                viewModel.startGame()
                findViewById<SeekBar>(R.id.seekBar).setEnabled(false);
            }
            start_btn.text = text
            /////////////////////

        }

        findViewById<Button>(R.id.click_btn).setOnClickListener {
            viewModel.setUserAction(GameEvents.CLICK)

        }

        findViewById<SeekBar>(R.id.seekBar).setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Display the current progress of SeekBar
                findViewById<TextView>(R.id.seekBarVal).text = i.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Do something

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                viewModel.setMinScreamLimit(seekBar.progress)
            }
        })
        //////////////////////////


        findViewById<Button>(R.id.pull_btn).setOnClickListener {
            viewModel.setUserAction(GameEvents.PULL)
        }

        findViewById<Button>(R.id.scream_btn).setOnClickListener {
            viewModel.setUserAction(GameEvents.SCREAM)
        }

        if (ContextCompat.checkSelfPermission(
                this,
                RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(RECORD_AUDIO),
                RECORD_REQUEST_CODE
            )
        }
    }

//    private fun startGame() {
//        viewModel.startGame()
//        viewModel.currentEvent.observe(this, Observer<GameEvents> {
//            if (it == GameEvents.SHAKE)
//                Log.d("AAA", "Shake")
//        })
//    }


    //TODO: replace for something better
    private fun showErrorMessage(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }

    //TODO: replace for something better
    private fun showUserFailedDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.app_name)
            .setMessage("Your failed the mission!")
            .show()
    }
}
