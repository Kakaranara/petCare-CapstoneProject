package com.example.petcare.ui.main.story.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.petcare.data.StoryRepository
import com.example.petcare.data.repository.ProfileRepository
import com.example.petcare.ui.main.story.add.AddStoryViewModel
import com.example.petcare.ui.main.story.comment.CommentViewModel
import com.example.petcare.ui.main.story.detail.DetailViewModel

@Suppress("UNCHECKED_CAST")
class StoryViewModelFactory(private val repository: StoryRepository, private val profileRepository: ProfileRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryViewModel::class.java)){
            return StoryViewModel(repository, profileRepository) as T
        }
        return super.create(modelClass)
    }
}