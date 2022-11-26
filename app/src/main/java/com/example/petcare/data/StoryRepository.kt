package com.example.petcare.data

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.petcare.data.stori.*
import com.example.petcare.helper.Async
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import java.util.UUID
import kotlin.concurrent.timerTask
import kotlin.system.measureTimeMillis

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
            val uid = mAuth.currentUser?.uid.toString()
            mDatabase.document().set(story).await()
            emit(BaseResult.Success(true))
        }catch (e: Exception){
            emit(BaseResult.Error(e.message.toString()))
        }
    }

    fun updatePostComment(postId: String, countComment: Int): LiveData<Async<Boolean>> {
        val liveData = MutableLiveData<Async<Boolean>>(Async.Loading)
        try {
            rootRef.collection("stories").whereEqualTo("postId", postId).get().addOnCompleteListener { task->
                if (task.isSuccessful){
                    for (document in task.result){
                        val update: MutableMap<String, Any> = HashMap()
                        update["comment"] = countComment
                        rootRef.collection("stories").document(document.id).set(update, SetOptions.merge())
                        liveData.value = Async.Success(true)
                    }
                }else{
                    liveData.value = Async.Error(task.exception.toString())
                }
            }
        }catch (e: Exception){
            liveData.value = Async.Error(e.message.toString())
        }
        return liveData
    }

    fun getAllStory(): LiveData<Async<StoryResponse>> {
        val liveData = MutableLiveData<Async<StoryResponse>>(Async.Loading)
        try {
            val storyResponse = StoryResponse()
            rootRef.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
            rootRef.collection("stories")
                .whereLessThan("createdAt", System.currentTimeMillis())
                .get()
                .addOnCompleteListener { task->
                    if (task.isSuccessful){
                        val result = task.result
                        result?.let {
                            storyResponse.story = result.documents.mapNotNull { snapshot->
                                snapshot.toObject(Story::class.java)
                            }
                            liveData.value = Async.Success(storyResponse)
                        }
                    }else{
                        liveData.value = Async.Error(task.exception.toString())
                    }
                }

        }catch (e: Exception){
            liveData.value = Async.Error(e.message.toString())
            Log.e(TAG, "onFailure: ${e.message}")
        }

        return liveData
    }


    fun addComment(comment: Comment): LiveData<BaseResult<Comment>> = liveData {
        emit(BaseResult.Loading)
        try {
            rootRef.collection("comment").document().set(comment)
            emit(BaseResult.Success(comment))
        }catch (e: Exception){
            emit(BaseResult.Error(e.message.toString()))
        }
    }

    fun getAllComment(postId: String): LiveData<Async<CommentResponse>>{
        val liveData = MutableLiveData<Async<CommentResponse>>(Async.Loading)
        try {
            val commentResponse = CommentResponse()
            rootRef.collection("comment")
                .whereEqualTo("idPost", postId)
                .get()
                .addOnCompleteListener { task->
                    if (task.isSuccessful){
                        val result = task.result
                        result.let {
                            commentResponse.comments = result.documents.mapNotNull { snapshot->
                                snapshot.toObject(Comment::class.java)
                            }
                            liveData.value = Async.Success(commentResponse)
                        }
                    }else{
                        liveData.value = Async.Error(task.exception.toString())
                    }
                }
        }catch (e: Exception){
            liveData.value = Async.Error(e.message.toString())
        }

        return liveData
    }

    fun addLike(like: Like): LiveData<Async<Like>> = liveData{
        emit(Async.Loading)
        try {
            rootRef.collection("like").document().set(like)
            emit(Async.Success(like))
        }catch (e: Exception){
            emit(Async.Error(e.message.toString()))
        }
    }

//    fun setFavorite(story: Story, isLiked: Boolean){
//        story.isLiked = isLiked
//        mDatabase.document(story.uid.toString()).update("liked", story.isLiked)
//    }

    companion object{
        const val TAG = "StoryRepository"
    }


}