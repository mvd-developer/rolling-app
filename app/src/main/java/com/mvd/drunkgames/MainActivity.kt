package com.mvd.drunkgames

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.Menu
import android.view.MenuItem
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.airbnb.lottie.LottieAnimationView
import com.mvd.drunkgames.base.DialogCallback
import com.mvd.drunkgames.base.showErrorMessage
import com.mvd.drunkgames.modules.GameEvents
import com.mvd.drunkgames.preferences.SettingsActivity


class MainActivity : AppCompatActivity(), DialogCallback {


    private lateinit var viewModel: MainActivityViewModel
    private lateinit var tvButtonText: TextView
    private lateinit var lottieBtnStart: LottieAnimationView
    private lateinit var seekBar: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProviders.of(this)[MainActivityViewModel::class.java]

        val message = intent.getStringExtra(EXTRA_MESSAGE)
        if (message != null)
            viewModel.onLoginComplete(message)

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

        tvButtonText = findViewById(R.id.tv_btn_text)
        tvButtonText.setText(R.string.start_game)

        lottieBtnStart = findViewById(R.id.btn_start)

        lottieBtnStart.setOnClickListener {
            if (viewModel.isGameStarted) {
                viewModel.setUserAction(GameEvents.CLICK)
            } else {
                viewModel.startGame()
            }
        }

        seekBar = findViewById(R.id.pull_btn)


//        findViewById<Button>(R.id.pull_btn).setOnClickListener {
//            viewModel.setUserAction(GameEvents.PULL)
//        }

//        findViewById<Button>(R.id.scream_btn).setOnClickListener {
//            viewModel.setUserAction(GameEvents.SCREAM)
//        }


//        val dialog = Dialog(this)
//        dialog.requestWindowFeature(FEATURE_NO_TITLE)
//        dialog.setContentView(R.layout.dialog_failed)
//        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        dialog.show()
    }


    //TODO: replace for something better
    private fun showUserFailedDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.app_name)
            .setMessage("Your failed the mission!")
            .setPositiveButton(android.R.string.ok) { dialog: DialogInterface, _ ->
                dialog.dismiss()
            }
            .show()
    }


    override fun playAgain() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun cancel() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
