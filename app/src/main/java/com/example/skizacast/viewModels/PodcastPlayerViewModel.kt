package com.example.skizacast.viewModels

import android.content.Context
import android.media.session.MediaController
import android.net.Uri
import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.analytics.AnalyticsListener
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionToken
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PodcastPlayerViewModel @Inject constructor(
    private val exoPlayer: ExoPlayer,
    private val mediaSession: MediaSession
) : ViewModel() {

    fun initializePlayer(context: Context){
        // Ensure ExoPlayer is properly initialized
        if (exoPlayer.playbackState == ExoPlayer.STATE_IDLE) {
            Log.d("PodcastPlayerViewModel", "Initializing ExoPlayer")
            mediaSession // This ensures the MediaSession is created and bound
        }
    }

    fun releasePlayer() {
        Log.d("PodcastPlayerViewModel", "Releasing ExoPlayer")
        exoPlayer.playWhenReady = false
        exoPlayer.release()
    }


    fun startPodcast(context: Context){
        Log.d("PodcastPlayerViewModel", "Playing Podcast")
        exoPlayer.apply {
            stop()
            clearMediaItems()
            val mediaItem = MediaItem.fromUri(Uri.parse("https://dts.podtrac.com/redirect.mp3/chrt.fm/track/288D49/stitcher.simplecastaudio.com/3bb687b0-04af-4257-90f1-39eef4e631b6/episodes/9a1ec5cb-405a-4ebb-8569-fced57c3d129/audio/128/default.mp3?aid=rss_feed&awCollectionId=3bb687b0-04af-4257-90f1-39eef4e631b6&awEpisodeId=9a1ec5cb-405a-4ebb-8569-fced57c3d129&feed=BqbsxVfO"))
            Log.d("PodcastPlayerViewModel", "Media Item URI: $mediaItem.uri")
            setMediaItem(mediaItem)
            playWhenReady = true
            prepare()
            play()
            Log.d("PodcastPlayerViewModel", "Podcast is prepared and playing")

            addAnalyticsListener(object : AnalyticsListener{
                @OptIn(UnstableApi::class)
                override fun onPlayerError(
                    eventTime: AnalyticsListener.EventTime,
                    error: PlaybackException
                ) {
                    Log.d("PodcastPlayerViewModel", "Player error: ${error.message}", error)
                }
            })
        }
    }
}