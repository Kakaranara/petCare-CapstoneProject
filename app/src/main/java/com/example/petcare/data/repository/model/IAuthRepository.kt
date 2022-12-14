package com.example.petcare.data.repository.model

import androidx.lifecycle.LiveData
import com.example.petcare.data.User
import com.example.petcare.helper.Async
import com.google.firebase.auth.AuthCredential

interface IAuthRepository {
    fun loginEmail(email: String, password: String): LiveData<Async<Unit>>
    fun registerEmail(email: String, password: String, name: String): LiveData<Async<Unit>>
    fun googleOneTapLogin(credential : AuthCredential) : LiveData<Async<User>>
}