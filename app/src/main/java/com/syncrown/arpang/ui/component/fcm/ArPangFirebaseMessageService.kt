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
import com.syncrown.arpang.ui.component.splash.SplashActivity

class ArPangFirebaseMessageService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.e("jung","fcm notification title : ${remoteMessage.notification?.title}")
        Log.e("jung","fcm notification body : ${remoteMessage.notification?.body}")
        Log.e("jung","fcm data title : ${remoteMessage.data["title"]}")
        Log.e("jung","fcm data body : ${remoteMessage.data["body"]}")
        // FCM 메시지 처리 로직
        remoteMessage.data.let { data ->
            val title = data["title"]
            val body = data["body"]

            if (title != null && body != null) {
                sendNotification(title, body)
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    private fun sendNotification(title: String?, message: String?) {
        val channelId = "arpang_channel_id"
        val channelName = "ArPang Channel"

        val intent = Intent(this, SplashActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_MUTABLE)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@ArPangFirebaseMessageService,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                return
            }
            val notificationId = System.currentTimeMillis().toInt() // 쌓이도록 하려면 이걸 id로 변경
            notify(0, notificationBuilder.build())
        }
    }
}