package com.example.petcare.data.repository

import android.util.Log
import com.example.petcare.data.remote.response.Schedule
import com.example.petcare.data.repository.model.IScheduleRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ScheduleRepository(
    private val auth: FirebaseAuth = Firebase.auth,
    private val fireStore: FirebaseFirestore = Firebase.firestore,
    private val scheduleRefs: CollectionReference = fireStore.collection("schedule")
) : IScheduleRepository {

    override fun listenData() {
        scheduleRefs
            .whereEqualTo("userId", auth.currentUser?.uid)
            .addSnapshotListener { querySnapshot: QuerySnapshot?, firebaseFirestoreException: FirebaseFirestoreException? ->

            }
    }

    override fun addSchedule(schedule: Schedule) {
        schedule.userId = auth.currentUser?.uid

        scheduleRefs
            .whereEqualTo("userId", auth.currentUser?.uid)
            .orderBy("timeStamp", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnFailureListener {
                Log.e(TAG, "addSchedule: FAILED :${it.message}")
            }
            .addOnSuccessListener {
                Log.d(TAG, "addSchedule: SUCCESS READ LATEST DOCS")
                val document = it.toObjects(Schedule::class.java)
                val id: Int = if (document.isEmpty()) {
                    0
                } else {
                    document[0].id ?: 1
                }
                schedule.id = id + 1

                scheduleRefs.add(schedule).addOnCompleteListener {
                    Log.d(TAG, "addSchedule: COMPLETED")
                }.addOnFailureListener {
                    Log.e(TAG, "addSchedule: Failed. ${it.message}")
                }.addOnSuccessListener {
                    Log.d(TAG, "addSchedule: SUCCESS")
                }

            }
    }


    companion object {
        private const val TAG = "ScheduleRepository"
    }
}