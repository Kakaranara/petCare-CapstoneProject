package com.example.petcare.data.repository.model

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.petcare.data.remote.response.Schedule
import com.example.petcare.helper.Async
import com.google.firebase.firestore.QuerySnapshot

interface IScheduleRepository {
    fun listenData() : LiveData<Async<QuerySnapshot?>>
    fun addSchedule(schedule: Schedule, context: Context)
    fun deleteData(documentId : String)
}