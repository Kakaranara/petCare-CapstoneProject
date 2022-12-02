package com.example.petcare.ui.main.story.main

import androidx.lifecycle.ViewModel
import com.example.petcare.data.StoryRepository

class StoryViewModel(private val storyRepository: StoryRepository): ViewModel() {

        fun getStories() =  storyRepository.getAllStory()

        suspend fun deleteStoryLike(postId: String, uid: String) = storyRepository.deletePostLike(postId, uid)

        suspend fun addStoryLike(postId: String, uid: String) = storyRepository.addPostLike(postId, uid)

        suspend fun addSharePost(postId: String, share: Int) = storyRepository.updateSharePost(postId, share)


}