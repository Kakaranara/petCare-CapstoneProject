package com.example.petcare.ui.main.schedule

import androidx.lifecycle.ViewModel
import com.example.petcare.data.repository.ScheduleRepository

class ScheduleViewModel(private val repository: ScheduleRepository = ScheduleRepository()) :
    ViewModel() {

    fun listenForDataChanges() = repository.listenData()
    fun unRegister() = repository.unRegister()
    fun deleteData(documentId : String) = repository.deleteData(documentId)
}