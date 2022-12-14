package com.example.petcare.ui.main.story.add

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.petcare.data.StoryRepository
import com.example.petcare.data.stori.Story

class AddStoryViewModel(private val repository: StoryRepository): ViewModel() {

    suspend fun postImage(name: String, imageUri: Uri) = repository.postImage(name, imageUri)
    suspend fun postStory(story: Story) = repository.addPost(story)
}