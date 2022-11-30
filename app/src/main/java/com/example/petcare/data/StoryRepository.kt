package com.example.petcare.data

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.petcare.data.stori.*
import com.example.petcare.helper.Async
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Query

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
                .orderBy("createdAt", Query.Direction.DESCENDING)
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

    fun getDetailStory(postId: String): LiveData<Async<Story>>{
        val liveData = MutableLiveData<Async<Story>>(Async.Loading)
        try {
            rootRef.collection("stories")
                .whereEqualTo("postId", postId)
                .get()
            var story: Story? = null
            rootRef.collection("stories")
                .whereEqualTo("postId", postId)
                .get()
                .addOnSuccessListener { snapshot->
                    if (snapshot != null){
                        val data = snapshot.toObjects(Story::class.java)[0]
                        story = Story(
                            data.postId, data.uid, data.name, data.avatarUrl, data.urlImg, data.description, data.createdAt, data.comment, data.like
                        )
                        liveData.value = Async.Success(story!!)
                    }else{
                        liveData.value = Async.Error("no data exists")
                    }
                }
        }catch (e: Exception){
            liveData.value = Async.Error(e.message.toString())
        }

        return liveData
    }

    fun addPostLike(postId: String, uid: String):LiveData<Async<Boolean>>{
        val liveData = MutableLiveData<Async<Boolean>>(Async.Loading)
        try {
            rootRef.collection("stories")
                .whereEqualTo("postId", postId)
                .get()
                .addOnCompleteListener { task->
                    if (task.isSuccessful){
                        for (document in task.result){
                            rootRef.collection("stories").document(document.id).update("like", FieldValue.arrayUnion(uid))
                            liveData.value = Async.Success(true)
                        }

                    }else{
                        liveData.value = Async.Error(task.exception!!.message.toString())
                    }
                }
        }catch (e:Exception){
            liveData.value = Async.Error(e.message.toString())

        }
        return liveData

    }

    fun deletePostLike(postId: String, uid: String): LiveData<Async<Boolean>>{
        val liveData = MutableLiveData<Async<Boolean>>(Async.Loading)
        try {
            rootRef.collection("stories")
                .whereEqualTo("postId", postId)
                .get()
                .addOnCompleteListener { task->
                    if (task.isSuccessful){
                        for (document in task.result){
                            rootRef.collection("stories").document(document.id).update("like", FieldValue.arrayRemove(uid))
                            liveData.value = Async.Success(true)
                        }
                    }else{
                        liveData.value = Async.Error(task.exception?.message.toString())
                    }
                }
        }catch (e: Exception){
            liveData.value = Async.Error(e.message.toString())
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

    companion object{
        const val TAG = "StoryRepository"
    }


}