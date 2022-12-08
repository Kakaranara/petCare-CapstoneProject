package com.example.petcare.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.petcare.data.User
import com.example.petcare.data.repository.model.IAuthRepository
import com.example.petcare.helper.Async
import com.example.petcare.ui.user.auth.login.LoginFragment
import com.example.petcare.ui.user.auth.register.RegisterFragment
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class AuthRepository(private val auth: FirebaseAuth = Firebase.auth, private val rootRef: FirebaseFirestore = FirebaseFirestore.getInstance()) : IAuthRepository {

    override fun loginEmail(email: String, password: String): LiveData<Async<Unit>> {
        val liveData = MutableLiveData<Async<Unit>>()
        liveData.value = Async.Loading
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                liveData.postValue(Async.Success(Unit))
                Log.d(LoginFragment.TAG, "onClick: login success")
            } else {
                liveData.postValue(Async.Error(task.exception?.message.toString()))
                Log.d(LoginFragment.TAG, "onClick: login failed. exception : ${task.exception}")
            }
        }
        return liveData
    }

    override fun registerEmail(
        email: String,
        password: String,
        name: String
    ): LiveData<Async<Unit>> {
        val liveData = MutableLiveData<Async<Unit>>(Async.Loading)

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(RegisterFragment.TAG, "onClick: Login success.")
                    val user = auth.currentUser
                    val profileUpdate = userProfileChangeRequest {
                        displayName = name
                    }
                    user?.updateProfile(profileUpdate)
                    liveData.postValue(Async.Success(Unit))
                } else {
                    liveData.postValue(Async.Error(task.exception?.message.toString()))
                    Log.d(
                        RegisterFragment.TAG,
                        "onClick: Fail login. Exception : ${task.exception}"
                    )
                }
            }

        return liveData
    }

    override fun googleOneTapLogin(credential: AuthCredential): LiveData<Async<User>> {
        val liveData = MutableLiveData<Async<User>>(Async.Loading)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser = auth.currentUser!!
                    val user = User(
                        currentUser.uid, currentUser.displayName, currentUser.email, currentUser.photoUrl.toString(), System.currentTimeMillis()
                    )
                    liveData.postValue(Async.Success(user))
                    Log.d(LoginFragment.TAG, "firebaseAuthWithGoogle: Success")
                } else {
                    liveData.postValue(Async.Error(task.exception?.message.toString()))
                    Log.d(LoginFragment.TAG, "firebaseAuthWithGoogle: FAILED ${task.exception}")
                }
            }
        return liveData
    }


}