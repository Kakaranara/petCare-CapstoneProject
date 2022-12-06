package com.example.petcare.ui.main.schedule

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.petcare.data.repository.ScheduleRepository
import com.example.petcare.helper.Async
import com.google.firebase.firestore.QuerySnapshot

class ScheduleViewModel(private val repository: ScheduleRepository = ScheduleRepository()) :
    ViewModel() {

    private val isLoginListener: MutableLiveData<Boolean> = MutableLiveData(false)

    val overviewListener: LiveData<Async<QuerySnapshot?>> =
        Transformations.switchMap(isLoginListener) {
            when (it) {
                true -> {
                    repository.listenOverviewSchedule()

                }
                false -> {
                    repository.unRegisterOveriewListener()
                    MutableLiveData()
                }
            }
        }

    val allScheduleListener: LiveData<Async<QuerySnapshot?>> =
        Transformations.switchMap(isLoginListener) {
            when (it) {
                true -> {
                    repository.listenAllData()
                }
                false -> {
                    repository.unRegisterAllScheduleListener()
                    MutableLiveData()
                }
            }
        }


    fun setHasLogin() {
        isLoginListener.value = true
    }

    fun setHasLogout() {
        isLoginListener.value = false
    }

    fun stopListeningOverview() {
        repository.unRegisterOveriewListener()
    }

    fun stopListeningAllSchedule() {
        repository.unRegisterAllScheduleListener()
    }

    fun deleteData(context: Context, id: Int, documentId: String) =
        repository.deleteData(context, id, documentId)
}