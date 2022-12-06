package com.example.petcare.ui.main.schedule

import android.content.Context
import androidx.lifecycle.*
import com.example.petcare.data.repository.ScheduleRepository
import com.example.petcare.helper.Async
import com.example.petcare.preferences.SchedulePreferences
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.launch

class ScheduleViewModel(
    private val repository: ScheduleRepository = ScheduleRepository(),
    private val preferences: SchedulePreferences
) :
    ViewModel() {

    private val isLoginListener: MutableLiveData<Boolean> = MutableLiveData(false)
    val isDialogAlreadyShow = preferences.isDialogShow().asLiveData()

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


    fun setDialogHasShown() {
        viewModelScope.launch {
            preferences.setDialogShown()
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

@Suppress("UNCHECKED_CAST")
class ScheduleVMFactory(private val schedulePreferences: SchedulePreferences) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScheduleViewModel::class.java)) {
            return ScheduleViewModel(preferences = schedulePreferences) as T
        }
        return super.create(modelClass)
    }
}