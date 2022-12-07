package com.example.petcare.di

import com.example.petcare.data.StoryRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

object Injection {
    fun provideStoryRepository(): StoryRepository{
        val rooRef : FirebaseFirestore = FirebaseFirestore.getInstance()
        val mStorage: StorageReference = FirebaseStorage.getInstance().reference
        return StoryRepository(rooRef, mStorage)
    }
}