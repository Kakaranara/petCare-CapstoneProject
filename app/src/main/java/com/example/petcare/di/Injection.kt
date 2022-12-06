package com.example.petcare.di

import com.example.petcare.data.StoryRepository
import com.example.petcare.data.repository.ProfileRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

object Injection {
    fun provideStoryRepository(): StoryRepository{
        val rooRef : FirebaseFirestore = FirebaseFirestore.getInstance()
        val mStorage: StorageReference = FirebaseStorage.getInstance().reference
        return StoryRepository(rooRef, mStorage)
    }
    fun provideProfileRepository(): ProfileRepository{
        val mAuth : FirebaseAuth = FirebaseAuth.getInstance()
        val rooRef : FirebaseFirestore = FirebaseFirestore.getInstance()
        return ProfileRepository(mAuth, rooRef)
    }
}