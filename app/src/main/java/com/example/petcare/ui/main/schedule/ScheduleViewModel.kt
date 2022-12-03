package com.example.petcare.ui.main.schedule

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.petcare.data.repository.ScheduleRepository
import com.example.petcare.helper.Async
import com.google.firebase.firestore.QuerySnapshot

class ScheduleViewModel(private val repository: ScheduleRepository = ScheduleRepository()) :
    ViewModel() {

    var overviewListener: LiveData<Async<QuerySnapshot?>> = MutableLiveData()
    var allScheduleListener: LiveData<Async<QuerySnapshot?>> = MutableLiveData()
    private val _isLoginListener: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoginListener: LiveData<Boolean> get() = _isLoginListener

    fun setHasLogin() {
        _isLoginListener.value = true
    }

    fun setHasLogout() {
        _isLoginListener.value = false
    }

    fun startListeningOverview() {
        overviewListener = repository.listenOverviewSchedule()
    }

    fun stopListeningOverview() {
        repository.unRegisterOveriewListener()
    }

    fun startListeningAllSchedule() {
        allScheduleListener = repository.listenAllData()
    }

    fun stopListeningAllSchedule() {
        repository.unRegisterAllScheduleListener()
    }

    fun deleteData(context: Context, id: Int, documentId: String) =
        repository.deleteData(context, id, documentId)
}