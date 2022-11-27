package com.example.petcare.ui.main.story.comment

import androidx.lifecycle.ViewModel
import com.example.petcare.data.StoryRepository
import com.example.petcare.data.stori.Comment
import com.example.petcare.data.stori.Story
import com.google.firebase.database.core.view.View

class CommentViewModel(private val repository: StoryRepository): ViewModel() {
    fun addComment(comment: Comment ) = repository.addComment(comment)

    fun getAllComment(postId: String) = repository.getAllComment(postId)

    fun updateComment(postId: String, commentCount: Int) = repository.updatePostComment(postId, commentCount)
}