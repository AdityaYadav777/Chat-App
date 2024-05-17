package com.aditya.chat_app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.aditya.chat_app.fragment.MainScreenActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class MYFirebaseMessagingService : FirebaseMessagingService() {
    private val channelId="adityayadav"

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val inte=Intent(this,MainScreenActivity::class.java)
        inte.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val mananger=getSystemService(Context.NOTIFICATION_SERVICE)
        createNotificationChannel(mananger as NotificationManager)

        val inte1=PendingIntent.getActivities(this,0, arrayOf(inte),PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this,channelId)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["message"])
            .setSmallIcon(R.drawable.chat)
            .setAutoCancel(true)
            .setContentIntent(inte1)
            .build()

        mananger.notify(Random.nextInt(),notification)

    }

    private fun createNotificationChannel(mananger: NotificationManager){

        val channel=NotificationChannel(channelId,"adityayadavchat",NotificationManager.IMPORTANCE_HIGH)

        channel.description="New Chat"
        channel.enableLights(true)

        mananger.createNotificationChannel(channel)


    }
}