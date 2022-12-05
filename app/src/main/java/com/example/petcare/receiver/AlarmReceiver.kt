package com.example.petcare.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Parcelable
import android.util.Log
import com.example.petcare.data.remote.response.Schedule
import com.example.petcare.helper.DateHelper
import com.example.petcare.service.NotificationService
import com.example.petcare.utils.ReminderParser
import java.util.*

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val schedule: Schedule? = intent.getParcel(EXTRA_SCHEDULE_PARCEL)
        val notification = NotificationService(context)

        notification.showNotification(schedule as Schedule)
    }

    fun setSchedule(context: Context, schedule: Schedule) {
        val additionTime = ReminderParser.getAdditionalTime(schedule.reminderBefore ?: "15 Menit")
        val wakeUpTime = (schedule.time ?: System.currentTimeMillis()) - additionTime
        Log.e(TAG, "setSchedule: wake me up in ${DateHelper.formatTime(Date(wakeUpTime))}")

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(EXTRA_SCHEDULE_PARCEL, schedule)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            schedule.id ?: 1,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmService = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmService.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            wakeUpTime,
            pendingIntent
        )
    }

    fun cancelAlarm(context: Context, id: Int) {
        val alarmService = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_IMMUTABLE)
        pendingIntent.cancel()

        alarmService.cancel(pendingIntent)
        Log.e(TAG, "cancelAlarm: alarm removed")
    }

    companion object {
        const val EXTRA_MESSAGE = "alarm_message"
        const val EXTRA_ID = "alarm_id"
        const val EXTRA_TIME = "alarm_time"
        const val EXTRA_SCHEDULE_PARCEL = "alarm_parcel_schedule"
        private const val TAG = "AlarmReceiver"
    }
}

//ext
inline fun <reified T : Parcelable> Intent.getParcel(key: String): T? = when {
    SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}