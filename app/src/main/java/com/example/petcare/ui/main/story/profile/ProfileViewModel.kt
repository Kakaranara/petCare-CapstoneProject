package com.example.petcare.ui.main.story.profile

import androidx.lifecycle.ViewModel
import com.example.petcare.data.StoryRepository

class ProfileViewModel(private val repository: StoryRepository): ViewModel() {
    fun getStoryById(uid: String) = repository.getStoryById(uid)
}