package com.example.petcare.service

import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.example.petcare.R

class NotificationService(private val context: Context) {

    private val manager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification(notifId: Int, message: String) {
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, SCHEDULE_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Reminder!")
            .setContentText(message)
            .setSound(soundUri)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        manager.notify(notifId, builder.build())
    }

    companion object {
        const val SCHEDULE_CHANNEL_ID = "notification_schedule"
        const val SCHEDULE_CHANNEL_NAME = "Scheduler"
    }
}