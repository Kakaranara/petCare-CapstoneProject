package com.example.petcare

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.petcare.data.StoryRepository
import com.example.petcare.ui.main.story.add.AddStoryViewModel
import com.example.petcare.ui.main.story.comment.CommentViewModel
import com.example.petcare.ui.main.story.detail.DetailViewModel
import com.example.petcare.ui.main.story.main.StoryViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.ktx.storageMetadata

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: StoryRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)){
            return AddStoryViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(CommentViewModel::class.java)){
            return CommentViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)){
            return DetailViewModel(repository) as T
        }
        return super.create(modelClass)
    }
}