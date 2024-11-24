package com.example.cinemamanagerapp

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.example.cinemamanagerapp.ui.activities.MainActivity

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            val title = remoteMessage.notification?.title
            val body = remoteMessage.notification?.body

            // Hiển thị thông báo
            sendNotification(title, body)
        }
    }

    private fun sendNotification(title: String?, body: String?) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannelId = "default_channel"

        val notificationBuilder = NotificationCompat.Builder(this, notificationChannelId)
            .setSmallIcon(R.drawable.alert)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)

        // Hiển thị thông báo
        notificationManager.notify(0, notificationBuilder.build())
    }
}
