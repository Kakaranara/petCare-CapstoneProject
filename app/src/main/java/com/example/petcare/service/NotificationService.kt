package com.example.petcare.service

import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.example.petcare.R
import com.example.petcare.data.remote.response.Schedule
import java.text.SimpleDateFormat
import java.util.*

class NotificationService(private val context: Context) {

    private val manager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val dateFormat = SimpleDateFormat("HH:mm a", Locale.getDefault())

    fun showNotification(schedule: Schedule) {
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val dateTime = dateFormat.format(schedule.time!!)
        val title = "${schedule.name?.ifEmpty { "No Title" }} - ${schedule.category}"
        val builder = NotificationCompat.Builder(context, SCHEDULE_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(dateTime)
            .setSound(soundUri)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val postScript = schedule.postScript!!
        val desc = schedule.description!!

        if (postScript.isNotEmpty() || desc.isNotEmpty()) {
            val postScriptShow = if (postScript.isNotEmpty()) {
                "PS : $postScript"
            } else ""
            val spacer = if (desc.isNotEmpty()) "\n" else ""
            val addition = "$desc$spacer$postScriptShow \n$dateTime"
            builder.setStyle(NotificationCompat.BigTextStyle().bigText(addition))
        }

        manager.notify(schedule.id!!, builder.build())
    }

    companion object {
        const val SCHEDULE_CHANNEL_ID = "notification_schedule"
        const val SCHEDULE_CHANNEL_NAME = "Scheduler"
    }
}