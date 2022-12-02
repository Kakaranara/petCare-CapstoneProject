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
    private val rootRef: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val mStorage: StorageReference
){

    suspend fun postImage(name: String, imageUri: Uri):LiveData<Async<Uri>> = liveData{

        emit(Async.Loading)
        try {
            val downloadUrl = mStorage.child(name).putFile(imageUri).await()
                .storage.downloadUrl.await()
            emit(Async.Success(downloadUrl))
        }catch (e:Exception){
            emit(Async.Error(e.message.toString()))
        }

    }

    suspend fun addPost(story: Story):LiveData<Async<Boolean>> = liveData {
        emit(Async.Loading)
        try {
            rootRef.collection("stories").document().set(story).await()
            emit(Async.Success(true))
        }catch (e: Exception){
            emit(Async.Error(e.message.toString()))
        }
    }

    suspend fun updatePostComment(postId: String, countComment: Int): LiveData<Async<StoryResponse>> = liveData{
        val liveData = MutableLiveData<Async<StoryResponse>>(Async.Loading)
        try {
            val storyResponse = StoryResponse()
            rootRef.collection("stories").whereEqualTo("postId", postId).get().addOnCompleteListener { task->
                if (task.isSuccessful){
                    for (document in task.result){
                        val update: MutableMap<String, Any> = HashMap()
                        update["comment"] = countComment
                        rootRef.collection("stories").document(document.id).set(update, SetOptions.merge()).addOnSuccessListener {
                            rootRef.collection("stories").orderBy("createdAt", Query.Direction.DESCENDING)
                                .get()
                                .addOnCompleteListener { stories->
                                    if (stories.isSuccessful){
                                        val data = stories.result
                                        data?.let {
                                            storyResponse.story = data.documents.mapNotNull { snapshot->
                                                snapshot.toObject(Story::class.java)
                                            }
                                            liveData.postValue(Async.Success(storyResponse))
                                        }
                                    }else{
                                        liveData.postValue(Async.Error(stories.exception?.message.toString()))
                                    }
                                }
                        }
                    }
                }else{
                    liveData.value = Async.Error(task.exception.toString())
                }
            }
        }catch (e: Exception){
            liveData.value = Async.Error(e.message.toString())
        }
        emitSource(liveData)
    }

    fun getAllStory(): LiveData<Async<StoryResponse>> {
        val liveData = MutableLiveData<Async<StoryResponse>>(Async.Loading)
        try {
            val storyResponse = StoryResponse()
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
                            liveData.postValue(Async.Success(storyResponse))
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
                            data.postId, data.uid, data.name, data.avatarUrl, data.urlImg, data.description, data.createdAt, data.comment, data.share, data.like
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

    suspend fun addPostLike(postId: String, uid: String):LiveData<Async<StoryResponse>> = liveData{
        val liveData = MutableLiveData<Async<StoryResponse>>(Async.Loading)
        try {
            val storyResponse = StoryResponse()
            rootRef.collection("stories")
                .whereEqualTo("postId", postId)
                .get()
                .addOnCompleteListener { task->
                    if (task.isSuccessful){
                        for (document in task.result){
                            rootRef.collection("stories").document(document.id).update("like", FieldValue.arrayUnion(uid)).addOnSuccessListener {
                                rootRef.collection("stories").orderBy("createdAt", Query.Direction.DESCENDING)
                                    .get()
                                    .addOnCompleteListener { stories->
                                        if (stories.isSuccessful){
                                            val data = stories.result
                                            data?.let {
                                                storyResponse.story = data.documents.mapNotNull { snapshot->
                                                    snapshot.toObject(Story::class.java)
                                                }
                                                liveData.postValue(Async.Success(storyResponse))
                                            }
                                        }
                                    }
                            }
                        }

                    }else{
                        liveData.value = Async.Error(task.exception!!.message.toString())
                    }
                }
        }catch (e:Exception){
            liveData.value = Async.Error(e.message.toString())

        }
        emitSource(liveData)

    }

    suspend fun deletePostLike(postId: String, uid: String): LiveData<Async<StoryResponse>> = liveData{
        val liveData = MutableLiveData<Async<StoryResponse>>(Async.Loading)
        try {
            val storyResponse = StoryResponse()
            rootRef.collection("stories")
                .whereEqualTo("postId", postId)
                .get()
                .addOnCompleteListener { task->
                    if (task.isSuccessful){
                        for (document in task.result){
                            rootRef.collection("stories").document(document.id).update("like", FieldValue.arrayRemove(uid)).addOnSuccessListener {
                                rootRef.collection("stories").orderBy("createdAt", Query.Direction.DESCENDING)
                                    .get()
                                    .addOnCompleteListener { stories->
                                        if (stories.isSuccessful){
                                            val data = stories.result
                                            data?.let {
                                                storyResponse.story = data.documents.mapNotNull { snapshot->
                                                    snapshot.toObject(Story::class.java)
                                                }
                                                liveData.postValue(Async.Success(storyResponse))
                                            }
                                        }else{
                                            liveData.postValue(Async.Error(stories.exception?.message.toString()))
                                        }
                                    }
                            }
                        }
                    }else{
                        liveData.value = Async.Error(task.exception?.message.toString())
                    }
                }
        }catch (e: Exception){
            liveData.value = Async.Error(e.message.toString())
        }

        emitSource(liveData)
    }

    suspend fun updateSharePost(postId: String, share: Int): LiveData<Async<StoryResponse>> = liveData{
        val liveData = MutableLiveData<Async<StoryResponse>>(Async.Loading)
        try {
            val storyResponse = StoryResponse()
            rootRef.collection("stories")
                .whereEqualTo("postId", postId)
                .get()
                .addOnCompleteListener { task->
                    if (task.isSuccessful){
                        for (document in task.result){
                            val update: MutableMap<String, Any> = HashMap()
                            update["share"] = share
                            rootRef.collection("stories").document(document.id).set(update, SetOptions.merge()).addOnSuccessListener {
                                rootRef.collection("stories").orderBy("createdAt", Query.Direction.DESCENDING)
                                    .get()
                                    .addOnCompleteListener { stories->
                                        if (stories.isSuccessful){
                                            val data = stories.result
                                            data?.let {
                                                storyResponse.story = data.documents.mapNotNull { snapshot->
                                                    snapshot.toObject(Story::class.java)
                                                }
                                                liveData.postValue(Async.Success(storyResponse))
                                            }
                                        }else{
                                            liveData.postValue(Async.Error(stories.exception?.message.toString()))
                                        }
                                    }
                            }
                        }
                    }else{
                        liveData.value = Async.Error(task.exception?.message.toString())
                    }
                }

        }catch (e: Exception){
            liveData.value = Async.Error(e.message.toString())
        }

        emitSource(liveData)
    }


    suspend fun addComment(comment: Comment): LiveData<Async<CommentResponse>> = liveData {
        val liveData = MutableLiveData<Async<CommentResponse>>(Async.Loading)
        try {
            rootRef.collection("comment").document().set(comment).addOnSuccessListener {
                val commentResponse = CommentResponse()
                rootRef.collection("comment")
                    .whereEqualTo("idPost", comment.idPost)
                    .get()
                    .addOnCompleteListener { task->
                        if (task.isSuccessful){
                            val result = task.result
                            result.let {
                                commentResponse.comments = result.documents.mapNotNull { snapshot->
                                    snapshot.toObject(Comment::class.java)
                                }
                                liveData.postValue(Async.Success(commentResponse))
                            }
                        }else{
                            liveData.postValue(Async.Error(task.exception.toString()))
                        }
                    }
            }

        }catch (e: Exception){
            liveData.postValue(Async.Error(e.message.toString()))
        }
        emitSource(liveData)
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