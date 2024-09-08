package com.example.skizacast.player.notification

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.createBitmap
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerNotificationManager
import coil.Coil
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.skizacast.R

@UnstableApi
class SkizaNotificationAdapter(
    private val context: Context,
    private val pendingIntent: PendingIntent?
): PlayerNotificationManager.MediaDescriptionAdapter {
    override fun getCurrentContentTitle(player: Player): CharSequence =
        player.mediaMetadata.albumTitle ?: "Unknown"

    override fun createCurrentContentIntent(player: Player): PendingIntent? =
        pendingIntent

    override fun getCurrentContentText(player: Player): CharSequence? =
        player.mediaMetadata.displayTitle ?: "Unknown"

    override fun getCurrentLargeIcon(
        player: Player,
        callback: PlayerNotificationManager.BitmapCallback
    ): Bitmap? {
        Coil.apply {
            ImageRequest.Builder(context).data(player.mediaMetadata.artworkUri).build()
        }
        return null
    }
}