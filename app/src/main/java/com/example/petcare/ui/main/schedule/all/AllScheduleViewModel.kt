package com.example.petcare.ui.main.schedule.all

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.petcare.data.repository.ScheduleRepository

class AllScheduleViewModel(
    private val repository: ScheduleRepository = ScheduleRepository()
) : ViewModel() {
    fun listenData() = repository.listenAllData()
    fun unRegister() = repository.unRegisterAll()
    fun deleteData(context: Context, id: Int, documentId: String) =
        repository.deleteData(context, id, documentId)
}