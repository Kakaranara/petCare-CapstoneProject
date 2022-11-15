package com.example.petcare.helper

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.showToast(msg: String?, longToast: Boolean = false) {
    activity?.let { context ->
        if (longToast) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }
}