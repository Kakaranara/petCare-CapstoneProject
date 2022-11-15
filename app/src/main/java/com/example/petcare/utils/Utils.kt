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

    fun isRegisterValid(email: String, password: String, confirmPassword: String, name: String) {
        if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
            throw Exception("Please input all the field!")
        }
        if (password != confirmPassword) {
            throw Exception("Password did not match")
        }
    }
}