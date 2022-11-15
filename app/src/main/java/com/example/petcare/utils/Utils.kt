package com.example.petcare.utils

/**
 * * Insert Utility function here
 */

object AuthUtil {
    fun isLoginValid(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            throw Exception("Mail and Password should not be empty!")
        }
    }
}