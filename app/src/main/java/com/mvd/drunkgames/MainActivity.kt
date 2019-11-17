package com.mvd.drunkgames

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mvd.drunkgames.modules.GameEvents

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

        findViewById<Button>(R.id.start_btn).setOnClickListener {
            startGame()
        }

    }

    private fun startGame() {
        viewModel.startGame()
        viewModel.currentEvent.observe(this, Observer<GameEvents> {
            if (it == GameEvents.SHAKE)
                Log.d("AAA", "Shake")
        })
    }


    //TODO: replace for something better
    private fun showErrorMessage(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }
}
