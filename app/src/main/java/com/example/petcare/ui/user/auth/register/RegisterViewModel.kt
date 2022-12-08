package com.example.petcare.ui.user.auth.register

import androidx.lifecycle.ViewModel
import com.example.petcare.data.repository.AuthRepository

class RegisterViewModel(private val repository: AuthRepository = AuthRepository()) : ViewModel() {
    fun registerWithEmail(email: String, password: String, name: String) =
        repository.registerEmail(email, password, name)
}