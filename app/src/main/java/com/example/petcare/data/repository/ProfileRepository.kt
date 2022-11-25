package com.example.petcare.data.repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.petcare.data.repository.model.IProfileRepository
import com.example.petcare.helper.Async
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase

class ProfileRepository(private val auth: FirebaseAuth = Firebase.auth) : IProfileRepository {
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