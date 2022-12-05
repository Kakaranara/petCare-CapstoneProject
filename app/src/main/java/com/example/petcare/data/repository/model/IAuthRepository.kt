package com.example.petcare.data.repository.model

import android.net.Uri
import androidx.lifecycle.LiveData
import com.example.petcare.data.User
import com.example.petcare.helper.Async
import com.google.firebase.auth.AuthCredential

interface IAuthRepository {
    fun loginEmail(email: String, password: String): LiveData<Async<Unit>>
    fun registerEmail(email: String, password: String, name: String): LiveData<Async<User>>
    fun googleOneTapLogin(credential : AuthCredential) : LiveData<Async<String>>
    suspend fun addUserToFirestore(data: User): LiveData<Async<Unit>>
}