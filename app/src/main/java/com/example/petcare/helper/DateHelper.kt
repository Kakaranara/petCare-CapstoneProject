package com.example.petcare.helper

import java.text.SimpleDateFormat
import java.util.*

object DateHelper {
    /**
     * Notes
     * HH = 24 time based
     * hh = AM PM based
     * MM = month
     * mm = minute
     */
    fun formatDate(date: Date): String {
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return format.format(date)
    }

    fun formatTime(date: Date): String {
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        return format.format(date)
    }

    fun combineDateTime(date: String, time: String): String {
        return "$date $time"
    }

    fun fullParse(string: String): Date? {
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return format.parse(string)
    }

    fun parseDate(string: String): Date? {
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return format.parse(string)
    }

    fun parseTime(string: String): Date? {
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        return format.parse(string)
    }

    fun setDateCalendarInstance(year: Int, month: Int, day: Int): Calendar {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        return calendar
    }

    fun setTimeCalendarInstance(hour: Int, minute: Int): Calendar {
        val calendar = Calendar.getInstance()
        calendar.apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }
        return calendar
    }
}