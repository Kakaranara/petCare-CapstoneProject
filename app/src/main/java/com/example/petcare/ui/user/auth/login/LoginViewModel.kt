package com.example.petcare.ui.user.auth.login

import androidx.lifecycle.ViewModel
import com.example.petcare.data.repository.AuthRepository

class LoginViewModel(private val repository: AuthRepository = AuthRepository()) : ViewModel() {
    fun loginWithEmail(email: String, password: String) = repository.loginEmail(email, password)
}