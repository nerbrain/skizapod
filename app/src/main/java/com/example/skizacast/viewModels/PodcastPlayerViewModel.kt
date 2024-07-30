package com.example.skizacast.viewModels

import android.app.Application
import android.content.ComponentName
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.skizacast.player.service.PodcastService
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class PodcastPlayerViewModel(application: Application) : AndroidViewModel(application) {
    val TAG = "PodcastPlayerViewModel"
    private val _mediaController = MutableStateFlow<MediaController?>(null)
    val mediaController: StateFlow<MediaController?> = _mediaController.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private var controllerFuture: ListenableFuture<MediaController>

    private val playerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            updatePlaybackState()
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            _isPlaying.value = isPlaying
            Log.d("PodcastPlayerViewModel", "onIsPlayingChanged: $isPlaying")
        }

        override fun onEvents(player: Player, events: Player.Events) {
            if (events.contains(Player.EVENT_POSITION_DISCONTINUITY)) {
                // Handle track change
            }
        }
    }

    init {
        val sessionToken = SessionToken(
            application,
            ComponentName(application, PodcastService::class.java)
        )
        controllerFuture = MediaController.Builder(application, sessionToken).buildAsync()
        controllerFuture.addListener({
            viewModelScope.launch {
                _mediaController.update { controllerFuture.get() }
                _mediaController.value?.addListener(playerListener)
                updatePlaybackState()
            }
        }, MoreExecutors.directExecutor())
    }

    private fun updatePlaybackState() {
        viewModelScope.launch {
            _isPlaying.value = _mediaController.value?.isPlaying ?: false
            Log.d("PodcastPlayerViewModel", "updatePlaybackState: isPlaying = ${_isPlaying.value}")
        }
    }

    override fun onCleared() {
        super.onCleared()
        MediaController.releaseFuture(controllerFuture)
    }

    fun playPause() {
        Log.d(TAG, "playPause: ")
        viewModelScope.launch {
            _mediaController.value?.let { controller ->
                Log.d(TAG, "playPause: $controller")
                if (controller.isPlaying) {
                    controller.pause()
                } else {
                    controller.play()
                }
            }
        }
    }

    fun seekToNext() {
        viewModelScope.launch {
            _mediaController.value?.seekToNext()
        }
    }

    fun seekToPrevious() {
        viewModelScope.launch {
            _mediaController.value?.seekToPrevious()
        }
    }

    fun seekTo(position: Long) {
        viewModelScope.launch {
            _mediaController.value?.seekTo(position)
        }
    }
}

//@HiltViewModel
//class PodcastPlayerViewModel @Inject constructor(
//    private val exoPlayer: ExoPlayer,
//    private val mediaSession: MediaSession
//) : ViewModel() {
//
//    fun initializePlayer(context: Context){
//        // Ensure ExoPlayer is properly initialized
//        if (exoPlayer.playbackState == ExoPlayer.STATE_IDLE) {
//            Log.d("PodcastPlayerViewModel", "Initializing ExoPlayer")
//            mediaSession // This ensures the MediaSession is created and bound
//        }
//    }
//
//    fun releasePlayer() {
//        Log.d("PodcastPlayerViewModel", "Releasing ExoPlayer")
//        exoPlayer.playWhenReady = false
//        exoPlayer.release()
//    }
//
//
//    fun startPodcast(context: Context){
//        Log.d("PodcastPlayerViewModel", "Playing Podcast")
//        exoPlayer.apply {
//            stop()
//            clearMediaItems()
//            val mediaItem = MediaItem.fromUri(Uri.parse("https://dts.podtrac.com/redirect.mp3/chrt.fm/track/288D49/stitcher.simplecastaudio.com/3bb687b0-04af-4257-90f1-39eef4e631b6/episodes/9a1ec5cb-405a-4ebb-8569-fced57c3d129/audio/128/default.mp3?aid=rss_feed&awCollectionId=3bb687b0-04af-4257-90f1-39eef4e631b6&awEpisodeId=9a1ec5cb-405a-4ebb-8569-fced57c3d129&feed=BqbsxVfO"))
//            Log.d("PodcastPlayerViewModel", "Media Item URI: $mediaItem.uri")
//            setMediaItem(mediaItem)
//            playWhenReady = true
//            prepare()
//            play()
//            Log.d("PodcastPlayerViewModel", "Podcast is prepared and playing")
//        }
//    }
//}