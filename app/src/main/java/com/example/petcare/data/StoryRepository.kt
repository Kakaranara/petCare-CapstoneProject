package com.example.petcare.data

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.petcare.data.stori.Story
import com.example.petcare.data.stori.StoryResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await

class StoryRepository(
    private val mAuth: FirebaseAuth,
    private val rootRef: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private var mDatabase: CollectionReference,
    private val mStorage: StorageReference
){

    fun postImage(imageUri: Uri):LiveData<BaseResult<Uri>> = liveData{

        emit(BaseResult.Loading)
        try {
            val downloadUrl = mStorage.child("Photo").putFile(imageUri).await()
                .storage.downloadUrl.await()
            emit(BaseResult.Success(downloadUrl))
        }catch (e:Exception){
            emit(BaseResult.Error(e.message.toString()))
        }

    }

    fun addPost(story: Story):LiveData<BaseResult<Boolean>> = liveData {
        emit(BaseResult.Loading)
        try {
            mDatabase.document().set(story).await()
            emit(BaseResult.Success(true))
        }catch (e: Exception){
            emit(BaseResult.Error(e.message.toString()))
        }
    }

    fun getAllStory(): MutableLiveData<StoryResponse> {
       val allStory = MutableLiveData<StoryResponse>()
        try {
            val storyResponse = StoryResponse()
            rootRef.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
            mDatabase
                .get()
                .addOnCompleteListener { task->
                    if (task.isSuccessful){
                        val result = task.result
                        result?.let {
                            storyResponse.story = result.documents.mapNotNull { snapshot->
                                snapshot.toObject(Story::class.java)
                            }
                            allStory.value = storyResponse
                        }
                    }
                }

        }catch (e: Exception){
            Log.e(TAG, "onFailure: ${e.message}")
        }

        return allStory
    }

    companion object{
        const val TAG = "StoryRepository"
    }


}