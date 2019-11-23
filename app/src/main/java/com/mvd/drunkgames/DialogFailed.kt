package com.mvd.drunkgames

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.mvd.drunkgames.base.DialogCallback


class DialogFailed : DialogFragment() {

    private lateinit var okButton: Button
    private lateinit var cancelButton: Button
    private var numberOfRounds = 0


    companion object {
        private const val NUMBER_OF_ROUNDS = "NUMBER_OF_ROUNDS"
        fun getInstance(numberOfRounds: Int, supportFragmentManager: FragmentManager): DialogFailed {
            val fragment = supportFragmentManager.findFragmentByTag(DialogFailed::class.java.name)
            val instance: DialogFailed
            instance = if (fragment is DialogFailed) {
                fragment
            } else {
                DialogFailed()
            }
            val bundle = Bundle()
            bundle.putInt(NUMBER_OF_ROUNDS, numberOfRounds)
            instance.arguments = bundle
            return instance
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_failed, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCanceledOnTouchOutside(false)


        okButton = view.findViewById(R.id.btn_ok)
        cancelButton = view.findViewById(R.id.btn_cancel)


        okButton.setOnClickListener {
            this.dismiss()
            (activity as DialogCallback).playAgain()
        }

        cancelButton.setOnClickListener {
            this.dismiss()
        }

        arguments?.let {
            numberOfRounds = it.getInt(NUMBER_OF_ROUNDS)
            val text = context?.getString(R.string.you_rounds_passed, numberOfRounds)
            view.findViewById<TextView>(R.id.tv_rounds).text = text
        }

        return view
    }

}