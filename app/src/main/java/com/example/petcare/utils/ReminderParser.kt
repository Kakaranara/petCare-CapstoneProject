package com.example.petcare.utils

object ReminderParser {

    val second: Long = 1 * 1000
    val minute: Long = second * 60
    val hour: Long = minute * 60
    val day: Long = hour * 24

    fun getAdditionalTime(remindBefore: String): Long {
        return when (remindBefore) {
            "15 Minutes" -> {
                15 * minute
            }
            "30 Minutes" -> {
                30 * minute
            }
            "1 Hours" -> {
                1 * hour
            }

            "2 Hours" -> {
                2 * hour
            }

            "4 Hours" -> {
                4 * hour
            }
            "8 Hours" -> {
                8 * hour
            }

            "1 Days" -> {
                1 * day
            }

            else -> 0
        }
    }
}