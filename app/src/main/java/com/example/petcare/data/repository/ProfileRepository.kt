package com.example.petcare.data.repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.petcare.data.User
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
    private val rootRef: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val storageRef: StorageReference = FirebaseStorage.getInstance().reference
) : IProfileRepository {


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
                                update["name"] = user.displayName.toString()
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


    override fun uploadPhotoToStorage(name: String, uri: Uri): LiveData<Async<Uri>> = liveData {
        emit(Async.Loading)
        try {
            val urlPhoto = storageRef.child(name).putFile(uri).await()
                .storage.downloadUrl.await()
            emit(Async.Success(urlPhoto))
        }catch (e: Exception){
            emit(Async.Error(e.toString()))
        }
    }

    companion object {
        const val TAG = "Profile Repository"
    }
}