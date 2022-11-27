package com.example.petcare.data.remote.response

data class Schedule(
    val name: String? = null,
    val category: String? = null,
    val description: String? = null,
    val reminderBefore: String? = null,
    val time: Long? = null,
    val timeStamp: Long? = null
)