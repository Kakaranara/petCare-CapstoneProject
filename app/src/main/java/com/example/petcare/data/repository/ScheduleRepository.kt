package com.example.petcare.data.repository

import android.util.Log
import com.example.petcare.data.remote.response.Schedule
import com.example.petcare.data.repository.model.IScheduleRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ScheduleRepository(
    private val auth: FirebaseAuth = Firebase.auth,
    private val fireStore: FirebaseFirestore = Firebase.firestore,
    private val scheduleRefs: CollectionReference = fireStore.collection("schedule")
) : IScheduleRepository {

    override fun listenData() {

    }

    override fun addSchedule(schedule: Schedule) {
        scheduleRefs.add(schedule).addOnCompleteListener {
            Log.d(TAG, "addSchedule: COMPLETED")
        }.addOnFailureListener {
            Log.e(TAG, "addSchedule: Failed. ${it.message}")
        }.addOnSuccessListener {
            Log.d(TAG, "addSchedule: SUCCESS")
        }
    }

    companion object {
        private const val TAG = "ScheduleRepository"
    }
}