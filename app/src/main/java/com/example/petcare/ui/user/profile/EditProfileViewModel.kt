package com.example.petcare.ui.user.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.petcare.data.repository.ProfileRepository
import com.example.petcare.data.repository.model.IProfileRepository

class EditProfileViewModel(
    private val repository: IProfileRepository = ProfileRepository()
) : ViewModel() {
    fun updateProfileData(name: String, uri: Uri?) = repository.updateProfile(name, uri)
    fun getUser() = repository.getUser()
}