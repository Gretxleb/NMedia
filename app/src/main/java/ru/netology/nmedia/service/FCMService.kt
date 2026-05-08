package ru.netology.nmedia.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.netology.nmedia.R
import ru.netology.nmedia.api.PostApi
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.dto.PushToken

class FCMService : FirebaseMessagingService() {
    private val channelId = "remote"

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_remote_name)
            val descriptionText = getString(R.string.channel_remote_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMessageReceived(message: RemoteMessage) {
        val data = message.data
        val recipientId = data["recipientId"]?.toLongOrNull()
        val authId = AppAuth.getInstance().authState.value?.id
        if (recipientId == null || recipientId == authId) {
            showNotification(message)
            return
        }
        resendPushToken()
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        sendPushToken(token)
    }

    @SuppressLint("MissingPermission")
    private fun showNotification(message: RemoteMessage) {
        val data = message.data
        val notification = message.notification
        val title = notification?.title ?: data["title"] ?: getString(R.string.app_name)
        val body = notification?.body ?: data["body"] ?: ""
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        NotificationManagerCompat.from(this).notify(1, builder.build())
    }

    private fun resendPushToken() {
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            sendPushToken(token)
        }
    }

    private fun sendPushToken(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                PostApi.sendPushToken(PushToken(token))
            } catch (_: Exception) {
            }
        }
    }
}