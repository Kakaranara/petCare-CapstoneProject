package com.example.petcare.ui.main.story.comment

import androidx.lifecycle.ViewModel
import com.example.petcare.data.StoryRepository
import com.example.petcare.data.stori.Comment

class CommentViewModel(private val repository: StoryRepository): ViewModel() {
    suspend fun addComment(comment: Comment ) = repository.addComment(comment)

    fun getAllComment(postId: String) = repository.getAllComment(postId)

    suspend fun updatePostComment(postId: String, commentCount: Int) = repository.updatePostComment(postId, commentCount)
}