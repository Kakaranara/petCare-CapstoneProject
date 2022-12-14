package com.example.petcare.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.petcare.data.remote.response.Schedule
import com.example.petcare.data.repository.model.IScheduleRepository
import com.example.petcare.helper.Async
import com.example.petcare.helper.DateHelper
import com.example.petcare.receiver.AlarmReceiver
import com.example.petcare.ui.main.schedule.ScheduleFragment
import com.example.petcare.utils.ReminderParser
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

    var listener: ListenerRegistration? = null
    var listenerAll: ListenerRegistration? = null

    override fun listenOverviewSchedule(): LiveData<Async<QuerySnapshot?>> {
        val liveData = MutableLiveData<Async<QuerySnapshot?>>(Async.Loading)
        listener = scheduleRefs
            .whereEqualTo("userId", auth.currentUser?.uid)
            .whereGreaterThanOrEqualTo("date", DateHelper.getTodayMillis())
            .whereLessThan("date", DateHelper.getTodayMillis() + ReminderParser.day * 7)
            .addSnapshotListener { snapshot: QuerySnapshot?, exception: FirebaseFirestoreException? ->
                if (exception != null) {
                    Log.e(ScheduleFragment.TAG, "onViewCreated: EXception in ${exception.message}")
                    liveData.postValue(Async.Error(exception.message.toString()))
                }

                liveData.postValue(Async.Success(snapshot))

                for (a in snapshot!!) {
                    Log.i(ScheduleFragment.TAG, "data in snapshot: ${a.data}")
                }

                for (dc in snapshot.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> Log.w(
                            ScheduleFragment.TAG,
                            "New city: ${dc.document.data}"
                        )
                        DocumentChange.Type.MODIFIED -> Log.d(
                            ScheduleFragment.TAG,
                            "Modified city: ${dc.document.data}"
                        )
                        DocumentChange.Type.REMOVED -> Log.e(
                            ScheduleFragment.TAG,
                            "Removed city: ${dc.document.data}"
                        )
                    }
                }
            }
        return liveData
    }

    fun unRegisterOveriewListener() {
        listener?.remove()
    }

    override fun listenAllData(): LiveData<Async<QuerySnapshot?>> {
        val liveData = MutableLiveData<Async<QuerySnapshot?>>()

        listenerAll = scheduleRefs
            .whereEqualTo("userId", auth.currentUser?.uid)
            .whereGreaterThanOrEqualTo("time", DateHelper.getTodayMillis())
            .orderBy("time", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    liveData.postValue(Async.Error(e.message.toString()))
                    Log.d(TAG, "listenAllData: Error listening all schedule. ${e.message}")
                }
                liveData.postValue(Async.Success(snapshot))
                for (dc in snapshot!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> Log.w(
                            ScheduleFragment.TAG,
                            "New city: ${dc.document.data}"
                        )
                        DocumentChange.Type.MODIFIED -> Log.d(
                            ScheduleFragment.TAG,
                            "Modified city: ${dc.document.data}"
                        )
                        DocumentChange.Type.REMOVED -> Log.e(
                            ScheduleFragment.TAG,
                            "Removed city: ${dc.document.data}"
                        )
                    }
                }
            }

        return liveData
    }

    fun unRegisterAllScheduleListener() {
        listenerAll?.remove()
    }

    override fun addSchedule(schedule: Schedule, context: Context) {
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

                val alarm = AlarmReceiver()
                alarm.setSchedule(context, schedule)

//                scheduleRefs.add(schedule)
                scheduleRefs.add(schedule).addOnCompleteListener {
                    Log.d(TAG, "addSchedule: COMPLETED")
                }.addOnFailureListener {
                    Log.e(TAG, "addSchedule: Failed. ${it.message}")
                }.addOnSuccessListener {
                    Log.d(TAG, "addSchedule: SUCCESS")
                }
            }
    }

    override fun deleteData(context: Context, id: Int, documentId: String) {

        scheduleRefs.document(documentId)
            .delete()
            .addOnSuccessListener {
                val alarm = AlarmReceiver()
                alarm.cancelAlarm(context, id)
                Log.d(TAG, "deleteData: Success")
            }.addOnFailureListener {
                Log.e(TAG, "deleteData: Fail. ${it.message}")
            }
    }

    override fun editData(context : Context, schedule: Schedule) {
        val docRefs = scheduleRefs.document(schedule.uniqueId!!)
        val alarm = AlarmReceiver()

        /**
         * ! note that im not canceling alarm in onSuccessListener
         * ! Because my feature depends on cache / offline-first.
         * ? onSuccessListener is not called when user is offline.
         */

        alarm.cancelAlarm(context, schedule.id!!)
        alarm.setSchedule(context, schedule)
        fireStore.runTransaction {
            it.update(docRefs, "name", schedule.name)
            it.update(docRefs, "category", schedule.category)
            it.update(docRefs, "description", schedule.description)
            it.update(docRefs, "reminderBefore", schedule.reminderBefore)
            it.update(docRefs, "postScript", schedule.postScript)
            it.update(docRefs, "date", schedule.date)
            it.update(docRefs, "time", schedule.time)
            it.update(docRefs, "timeStamp", schedule.timeStamp)
        }.addOnSuccessListener {
            Log.d(TAG, "editData: EDIT SUCCESS")
        }.addOnFailureListener {
            Log.e(TAG, "editData: ERROR ${it.message}")
        }
    }

    companion object {
        private const val TAG = "ScheduleRepository"
    }
}