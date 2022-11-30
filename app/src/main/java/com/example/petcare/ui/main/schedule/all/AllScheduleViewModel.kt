package com.example.petcare.ui.main.schedule.all

import androidx.lifecycle.ViewModel
import com.example.petcare.data.repository.ScheduleRepository

class AllScheduleViewModel(
    private val repository: ScheduleRepository = ScheduleRepository()
) : ViewModel() {
    fun listenData() = repository.listenAllData()
    fun unRegister() = repository.unRegisterAll()
    fun deleteData(documentId: String) = repository.deleteData(documentId)
}