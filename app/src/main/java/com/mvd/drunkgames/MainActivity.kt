package com.mvd.drunkgames

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window.FEATURE_NO_TITLE
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mvd.drunkgames.base.DialogCallback
import com.mvd.drunkgames.base.showErrorMessage
import com.mvd.drunkgames.modules.GameEvents


class MainActivity : AppCompatActivity(), DialogCallback {


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

//        findViewById<Button>(R.id.click_btn).setOnClickListener {
//            viewModel.setUserAction(GameEvents.CLICK)
//        }

//        findViewById<Button>(R.id.pull_btn).setOnClickListener {
//            viewModel.setUserAction(GameEvents.PULL)
//        }

//        findViewById<Button>(R.id.scream_btn).setOnClickListener {
//            viewModel.setUserAction(GameEvents.SCREAM)
//        }

        //Because we already have this module up and running
//        findViewById<Button>(R.id.shake_btn).setOnClickListener {
//            viewModel.setUserAction(GameEvents.SHAKE)
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

}
