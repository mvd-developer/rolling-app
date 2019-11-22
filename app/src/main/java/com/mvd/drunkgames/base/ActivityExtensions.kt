package com.mvd.drunkgames.base

import android.app.Activity
import android.content.DialogInterface
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.mvd.drunkgames.R

fun Activity.showErrorMessage(errorMessage: String) {
    AlertDialog.Builder(this)
        .setTitle(R.string.app_name)
        .setMessage(errorMessage)
        .setPositiveButton(android.R.string.ok) { dialog: DialogInterface, _ ->
            dialog.dismiss()
        }
        .show()
}

fun Activity.showErrorMessage(@StringRes resId: Int) {
    this.showErrorMessage(this.getString(resId))
}