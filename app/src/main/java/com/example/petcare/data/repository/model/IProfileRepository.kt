package com.example.petcare.data.repository.model

import android.net.Uri
import androidx.lifecycle.LiveData
import com.example.petcare.helper.Async
import com.google.firebase.auth.FirebaseUser

interface IProfileRepository {
    fun updateProfile(name: String, uri: Uri?) : LiveData<Async<Unit>>
    fun getUser() : FirebaseUser?
    fun uploadPhotoToStorage(name: String, uri: Uri): LiveData<Async<Uri>>
}