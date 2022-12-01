package com.example.petcare.ui.main.story.main

import androidx.lifecycle.ViewModel
import com.example.petcare.data.StoryRepository

class StoryViewModel(private val storyRepository: StoryRepository): ViewModel() {

        fun getStories() =  storyRepository.getAllStory()

        fun deleteStoryLike(postId: String, uid: String) = storyRepository.deletePostLike(postId, uid)

        fun addStoryLike(postId: String, uid: String) = storyRepository.addPostLike(postId, uid)

        fun addSharePost(postId: String, share: Int) = storyRepository.updateSharePost(postId, share)


}