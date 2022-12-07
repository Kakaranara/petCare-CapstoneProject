package com.example.petcare.ui.main.story.comment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petcare.data.StoryRepository
import com.example.petcare.data.stori.Comment
import com.example.petcare.data.stori.Story
import com.google.firebase.database.core.view.View
import kotlinx.coroutines.launch

class CommentViewModel(private val repository: StoryRepository): ViewModel() {
    suspend fun addComment(comment: Comment ) = repository.addComment(comment)

    fun getAllComment(postId: String) = repository.getAllComment(postId)

    suspend fun updatePostComment(postId: String, commentCount: Int) = repository.updatePostComment(postId, commentCount)
}