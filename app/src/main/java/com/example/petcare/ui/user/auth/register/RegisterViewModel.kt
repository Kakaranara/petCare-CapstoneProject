package com.example.petcare.ui.user.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petcare.data.User
import com.example.petcare.data.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel(private val repository: AuthRepository = AuthRepository()) : ViewModel() {
    fun registerWithEmail(email: String, password: String, name: String) =
        repository.registerEmail(email, password, name)
}