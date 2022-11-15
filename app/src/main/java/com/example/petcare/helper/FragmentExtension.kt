package com.example.petcare.helper

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Fragment.showToast(msg: String?, longToast: Boolean = false) {
    activity?.let { context ->
        when (longToast) {
            true -> Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
            false -> Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }
}

fun Fragment.showSnackbar(msg: String?, longDuration: Boolean = false) {
    view?.let { view ->
        when (longDuration) {
            true -> Snackbar.make(view, msg.toString(), Snackbar.LENGTH_LONG).show()
            false -> Snackbar.make(view, msg.toString(), Snackbar.LENGTH_SHORT).show()
        }
    }
}