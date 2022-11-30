package com.example.petcare.ui.main.story.detail

import androidx.lifecycle.ViewModel
import com.example.petcare.data.StoryRepository

class DetailViewModel(private val repository: StoryRepository): ViewModel() {
    fun getDetailPost(postId: String) = repository.getDetailStory(postId)

    fun deletePostLike(postId: String, uid: String) = repository.deletePostLike(postId, uid)

    fun addPostLike(postId: String, uid: String) = repository.addPostLike(postId, uid)

}