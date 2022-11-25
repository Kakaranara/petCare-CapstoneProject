package com.example.petcare.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.petcare.service.NotificationService

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra(EXTRA_MESSAGE)
        val id = intent.getIntExtra(EXTRA_ID, 0)

        val notif = NotificationService(context)
        notif.showNotification(id, message ?: "No message")
    }

    companion object {
        const val EXTRA_MESSAGE = "alarm_message"
        const val EXTRA_ID = "alarm_id"
    }
}