package com.example.petcare

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.petcare.data.StoryRepository
import com.example.petcare.di.Injection
import com.example.petcare.ui.main.other.petshop.PetShopViewModel
import com.example.petcare.ui.main.story.add.AddStoryViewModel
import com.example.petcare.ui.main.story.comment.CommentViewModel
import com.example.petcare.ui.main.story.detail.DetailViewModel
import com.example.petcare.ui.main.story.main.StoryViewModel
import com.example.petcare.ui.main.story.profile.ProfileViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: StoryRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            return StoryViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)) {
            return AddStoryViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(CommentViewModel::class.java)) {
            return CommentViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(PetShopViewModel::class.java)) {
            return PetShopViewModel(Injection.providePetShopRepository()) as T
        }
        return super.create(modelClass)
    }
}