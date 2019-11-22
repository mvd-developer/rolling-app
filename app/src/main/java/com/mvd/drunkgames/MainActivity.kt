package com.mvd.drunkgames

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mvd.drunkgames.preferences.SettingsFragment
import com.mvd.drunkgames.modules.GameEvents
import com.mvd.drunkgames.preferences.PrefsManager

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel

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


        findViewById<Button>(R.id.start_btn).setOnClickListener {
            viewModel.startGame()
        }

        findViewById<Button>(R.id.click_btn).setOnClickListener {
            viewModel.setUserAction(GameEvents.CLICK)
        }

        findViewById<Button>(R.id.pull_btn).setOnClickListener {
            viewModel.setUserAction(GameEvents.PULL)
        }

        findViewById<Button>(R.id.scream_btn).setOnClickListener {
            viewModel.setUserAction(GameEvents.SCREAM)
        }

        //Because we already have this module up and running
//        findViewById<Button>(R.id.shake_btn).setOnClickListener {
//            viewModel.setUserAction(GameEvents.SHAKE)
//        }
    }


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
