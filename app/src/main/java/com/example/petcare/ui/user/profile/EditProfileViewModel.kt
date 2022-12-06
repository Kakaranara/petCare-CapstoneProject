package com.example.petcare.ui.user.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petcare.data.repository.ProfileRepository
import com.example.petcare.data.repository.model.IProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val repository: IProfileRepository = ProfileRepository()
) : ViewModel() {
    fun updateProfileData(name: String, uri: Uri?) = repository.updateProfile(name, uri)
    fun getUser() = repository.getUser()
    fun updateUserToFirebase(name: String, uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUserToFirestore(name, uri)
        }
    }

    suspend fun postPhotoToStorage(name: String, uri: Uri) = repository.uploadPhotoToStorage(name, uri)
}