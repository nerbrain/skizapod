package com.example.skizacast.player.service

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.example.skizacast.modules.MediaSessionModule
import com.example.skizacast.player.notification.SkizaNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PodcastService: MediaSessionService() {


    @Inject
    lateinit var player: ExoPlayer
    @Inject
    lateinit var mediaSession: MediaSession
    @Inject
    lateinit var notificationManager: SkizaNotificationManager

//    override fun onCreate() {
//        super.onCreate()
//        player = MediaSessionModule.provideExoPlayer(this)
//        mediaSession = MediaSessionModule.provideMediaSession(this,player)
//
//        player.apply {
//            stop()
//            clearMediaItems()
//            val mediaItem = MediaItem.fromUri(Uri.parse("https://dts.podtrac.com/redirect.mp3/chrt.fm/track/288D49/stitcher.simplecastaudio.com/3bb687b0-04af-4257-90f1-39eef4e631b6/episodes/9a1ec5cb-405a-4ebb-8569-fced57c3d129/audio/128/default.mp3?aid=rss_feed&awCollectionId=3bb687b0-04af-4257-90f1-39eef4e631b6&awEpisodeId=9a1ec5cb-405a-4ebb-8569-fced57c3d129&feed=BqbsxVfO"))
//            Log.d("PodcastService", "Media Item URI: $mediaItem.uri")
//            setMediaItem(mediaItem)
//            playWhenReady = true
//            prepare()
//            play()
//            Log.d("PodcastService", "Podcast is prepared and playing")
//        }
//    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.startNotificationService(
                mediaSessionService = this,
                mediaSession = mediaSession
            )
        }
        return super.onStartCommand(intent, flags, startId)
    }


    override fun onBind(intent: Intent?): IBinder? {
        super.onBind(intent)
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaSession.apply {
            release()
            if (player.playbackState != Player.STATE_IDLE){
                player.seekTo(0)
                player.playWhenReady = false
                player.stop()
            }
        }
    }

    override fun onGetSession(
        controllerInfo: MediaSession.ControllerInfo
    ): MediaSession = mediaSession

}