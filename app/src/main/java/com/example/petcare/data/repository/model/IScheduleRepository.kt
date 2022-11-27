package com.example.petcare.data.repository.model

import androidx.lifecycle.LiveData
import com.example.petcare.data.remote.response.Schedule
import com.example.petcare.helper.Async

interface IScheduleRepository {
    fun listenData()
    fun addSchedule(schedule: Schedule)
}