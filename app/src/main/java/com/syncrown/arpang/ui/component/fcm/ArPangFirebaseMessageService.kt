package com.syncrown.arpang.ui.component.fcm

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.syncrown.arpang.R
import com.syncrown.arpang.db.push_db.PushMessageDatabase
import com.syncrown.arpang.db.push_db.PushMessageEntity
import com.syncrown.arpang.ui.component.splash.SplashActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArPangFirebaseMessageService : FirebaseMessagingService() {
    private val channelId = "arpang_channel_id"
    private val channelName = "ArPang Channel"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.e("jung", "fcm notification title : ${remoteMessage.notification?.title}")
        Log.e("jung", "fcm notification body : ${remoteMessage.notification?.body}")
        Log.e("jung", "fcm data title : ${remoteMessage.data["title"]}")
        Log.e("jung", "fcm data body : ${remoteMessage.data["body"]}")

        remoteMessage.data.let { data ->
            val title = data["title"]
            val body = data["body"]
            val imageUrl = data["imageUrl"]
            val timeStamp = System.currentTimeMillis()

            if (!title.isNullOrEmpty() && !body.isNullOrEmpty() && !imageUrl.isNullOrEmpty()) {
                sendNotification(title, body)
                insertToDatabase(title, body, imageUrl, timeStamp)
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("jung", "New token: $token")
    }

    private fun insertToDatabase(title: String, body: String, url: String, timeStamp: Long) {
        val pushMessageEntity = PushMessageEntity(
            id = 0,
            title = title,
            body = body,
            imageUrl = url,
            receiveTime = timeStamp
        )

        CoroutineScope(Dispatchers.IO).launch {
            val dao = PushMessageDatabase.getDatabase(applicationContext).pushMessageDao()
            dao.insertMessage(pushMessageEntity)
            Log.d("jung", "Message saved: $title")
        }
    }

    private fun sendNotification(title: String, message: String) {
        createNotificationChannel()

        val intent = Intent(this, SplashActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_MUTABLE
        )

        val notificationId = System.currentTimeMillis().toInt()

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@ArPangFirebaseMessageService,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.e("jung", "Permission not granted for posting notifications.")
                return
            }
            notify(notificationId, notificationBuilder.build())
        }
    }

    private fun createNotificationChannel() {
        val notificationManager = getSystemService(NotificationManager::class.java)
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "ArPang notifications"
        }
        notificationManager?.createNotificationChannel(channel)
    }
}