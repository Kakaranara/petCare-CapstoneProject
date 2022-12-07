package com.example.petcare.data.remote.response

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties
@Parcelize
data class Schedule(
    var userId: String? = null,
    var name: String? = null,
    var category: String? = null,
    var description: String? = null,
    var reminderBefore: String? = null,
    var postScript: String? = null,
    @get:Exclude var uniqueId: String? = null,
    var id: Int? = null,
    var date: Long? = null,
    var time: Long? = null,
    var timeStamp: Long? = null
) : Parcelable

data class GroupedSchedule(
    val date: String? = null,
    val schedule: List<Schedule>? = null
)