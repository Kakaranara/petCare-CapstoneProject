package com.example.petcare.data.remote.response

data class Schedule(
    var userId: String? = null,
    var name: String? = null,
    var category: String? = null,
    var description: String? = null,
    var reminderBefore: String? = null,
    var postScript: String? = null,
    var time: Long? = null,
    var timeStamp: Long? = null
)

data class GroupedSchedule(
    val date: String? = null,
    val schedule: List<Schedule>? = null
)