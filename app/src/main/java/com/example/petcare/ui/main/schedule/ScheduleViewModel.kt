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

    /**
     * login listener
     * is used to consider listening schedule in repository or not.
     * Bug can be happen if we're not doing this,
     * Because it still listen to the previous account.
     */
    private val isLoginListener: MutableLiveData<Boolean> = MutableLiveData(false)

    /**
     * dialog
     * This will listen to preference where we need to show dialog,
     * IF app is opened first time.
     * User can pick whether yes r no. ** user supposed to turn on notification setting **
     * At the end, it's user choice.
     */
    val isDialogAlreadyShow = preferences.isDialogShow().asLiveData()

    /**
     * This 2 listener will respond and depend on loginListener.
     * Login listener will be set to true if user already login.
     * Check main activity.
     */
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