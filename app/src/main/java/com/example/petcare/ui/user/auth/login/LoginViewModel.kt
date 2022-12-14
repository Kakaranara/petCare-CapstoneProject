package com.example.petcare.ui.user.auth.login

import androidx.lifecycle.ViewModel
import com.example.petcare.data.repository.AuthRepository
import com.google.firebase.auth.AuthCredential

class LoginViewModel(private val repository: AuthRepository = AuthRepository()) : ViewModel() {
    fun loginWithEmail(email: String, password: String) = repository.loginEmail(email, password)
    fun oneTapGoogleLogin(credential: AuthCredential) = repository.googleOneTapLogin(credential)
}