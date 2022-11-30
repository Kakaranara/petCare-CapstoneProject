package com.example.petcare.data.remote.response

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Schedule(
    var userId: String? = null,
    var name: String? = null,
    var category: String? = null,
    var description: String? = null,
    var reminderBefore: String? = null,
    var postScript: String? = null,
    @get:Exclude var uniqueId: String? = null,
    var id: Int? = null,
    val date: Long? = null,
    var time: Long? = null,
    var timeStamp: Long? = null
)

data class GroupedSchedule(
    val date: String? = null,
    val schedule: List<Schedule>? = null
)