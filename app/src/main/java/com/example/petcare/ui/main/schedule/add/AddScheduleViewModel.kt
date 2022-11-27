package com.example.petcare.ui.main.schedule.add

import androidx.lifecycle.ViewModel
import com.example.petcare.data.remote.response.Schedule
import com.example.petcare.data.repository.ScheduleRepository
import com.example.petcare.data.repository.model.IScheduleRepository

class AddScheduleViewModel(private val repository: ScheduleRepository = ScheduleRepository()) : ViewModel() {
    fun addData(schedule: Schedule) {
        repository.addSchedule(schedule)
    }
}