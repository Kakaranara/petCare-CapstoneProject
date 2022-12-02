package com.example.petcare.ui.main.schedule.edit

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.petcare.data.remote.response.Schedule
import com.example.petcare.data.repository.ScheduleRepository

class EditScheduleViewModel(private val repository: ScheduleRepository = ScheduleRepository()) : ViewModel() {
    fun edit(context: Context, schedule: Schedule) = repository.editData(context, schedule)
}