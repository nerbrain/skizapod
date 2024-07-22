package com.example.skizacast.player.notification

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val NOTIFICATION_ID = 101
private const val NOTIFICATION_CHANNEL_NAME =  "notification channel 1"
private const val NOTIFICATION_CHANNEL_ID =  "notification channel id 1"
class SkizaNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val exoPlayer: ExoPlayer
) {
    private val notificationManager: NotificationManagerCompat =
        NotificationManagerCompat.from(context)


}