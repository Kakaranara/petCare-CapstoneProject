package com.example.petcare.data.repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.petcare.data.repository.model.IProfileRepository
import com.example.petcare.helper.Async
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await

class ProfileRepository(
    private val auth: FirebaseAuth = Firebase.auth,
    private val mStorage: StorageReference = FirebaseStorage.getInstance().reference,
    private val rootRef: FirebaseFirestore = FirebaseFirestore.getInstance()
) : IProfileRepository {

    override fun postPhotoProfile(name: String, imgUri: Uri):LiveData<Async<Uri>> = liveData {
        emit(Async.Loading)
        try {
            val url = mStorage.child(name).putFile(imgUri).await()
                .storage.downloadUrl.await()
            emit(Async.Success(url))
        }catch (e: Exception){
            emit(Async.Error(e.message.toString()))
        }
    }


    override fun updateProfile(name: String, uri: Uri?): LiveData<Async<Unit>> {
        val liveData = MutableLiveData<Async<Unit>>(Async.Loading)
        val currentUser = getUser()
        currentUser?.let { user ->
            val updateRequest = userProfileChangeRequest {
                displayName = name
                photoUri = uri
            }
            user.updateProfile(updateRequest)
                .addOnSuccessListener {
                    liveData.postValue(Async.Success(Unit))
                    Log.d(TAG, "updateProfile: Success")
                    rootRef.collection("stories").whereEqualTo("uid", user.uid).get().addOnCompleteListener { task->
                        if (task.isSuccessful){
                            for (document in task.result){
                                val update: MutableMap<String, Any> = HashMap()
                                update["avatarUrl"] = user.photoUrl.toString()
                                rootRef.collection("stories").document(document.id).set(update, SetOptions.merge())
                            }
                        }
                    }
                }.addOnFailureListener { e ->
                    liveData.postValue(Async.Error(e.message.toString()))
                    Log.e(TAG, "updateProfile: Error in ${e.message}")
                    e.printStackTrace()
                }
        }
        return liveData
    }

    override fun getUser(): FirebaseUser? = auth.currentUser

    companion object {
        const val TAG = "Profile Repository"
    }
}