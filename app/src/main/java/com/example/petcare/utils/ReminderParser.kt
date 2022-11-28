package com.example.petcare.utils

object ReminderParser {
    fun getAdditionalTime(remindBefore: String): Long {
        return when (remindBefore) {
            "15 Minutes" -> {
                15 * 1000 * 60
            }
            "30 Minutes" -> {
                30 * 1000 * 60
            }
            "1 Hours" -> {
                1 * 1_000 * 60 * 60
            }

            "2 Hours" -> {
                2 * 1_000 * 60 * 60
            }

            "4 Hours" -> {
                4 * 1_000 * 60 * 60
            }
            "8 Hours" -> {
                8 * 1_000 * 60 * 60
            }

            "1 Day" -> {
                24 * 1_000 * 60 * 60
            }

            else -> 0
        }
    }
}