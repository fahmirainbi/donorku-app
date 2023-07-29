package com.example.donorku_app.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.os.Build
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.donorku_app.R
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class DonorkuMessagingService: FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        Log.d("DonorkuMessagingService", "Refreshed token: $token")

    }

    override fun onMessageReceived(message: RemoteMessage) {
        val title = message.notification?.title?:"Title Notifikasi"
        val content = message.notification?.body?:"Body Notifikasi"
        sendNotification(title,content)
    }

    @SuppressLint("MissingPermission")
    fun sendNotification(title:String, content:String){
        var builder = NotificationCompat.Builder(this, "Donorku-Channel")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        createNotificationChannel()
        with(NotificationManagerCompat.from(this)) {
            notify(1, builder.build())
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name:String = "Donorku-Channel"
            val descriptionText = "Donorku-Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("Donorku-Channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}