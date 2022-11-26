package com.example.petcare.ui.main.story.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petcare.data.StoryRepository
import com.example.petcare.data.stori.Like
import com.example.petcare.data.stori.Story
import com.google.firebase.database.*
import kotlinx.coroutines.launch

class StoryViewModel(private val storyRepository: StoryRepository): ViewModel() {

        fun getStories() =  storyRepository.getAllStory()

        fun likeStory(like: Like) = storyRepository.addLike(like)

//        fun likeStory(story: Story){
//                viewModelScope.launch {
//                        storyRepository.setFavorite(story, true)
//                }
//        }
//        fun unLikeStory(story: Story){
//                viewModelScope.launch {
//                        storyRepository.setFavorite(story, false)
//                }
//        }


}