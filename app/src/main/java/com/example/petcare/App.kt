package com.example.petcare

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.petcare.service.NotificationService

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        createScheduleNotificationChannel()
    }

    private fun createScheduleNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val id = NotificationService.SCHEDULE_CHANNEL_ID
            val name = NotificationService.SCHEDULE_CHANNEL_NAME
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(id, name, importance)
            manager.createNotificationChannel(channel)
        }
    }
}