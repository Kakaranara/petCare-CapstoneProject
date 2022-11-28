package com.example.petcare.data.repository.model

import android.content.Context
import com.example.petcare.data.remote.response.Schedule

interface IScheduleRepository {
    fun listenData()
    fun addSchedule(schedule: Schedule, context: Context)
}