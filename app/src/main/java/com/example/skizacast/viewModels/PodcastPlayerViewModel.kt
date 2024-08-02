package com.example.skizacast.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.skizacast.data.model.DummyData
import com.example.skizacast.data.model.Episode
import com.example.skizacast.player.service.PlayerEvent
import com.example.skizacast.player.service.PodcastAudioState
import com.example.skizacast.player.service.PodcastServiceHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.internal.concurrent.formatDuration
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private val podcastDummy = Episode(
    uri = "",
    title = "",
    id = "0",
    description = "",
    duration = "",
    image = ""
)


@HiltViewModel
class PodcastPlayerViewModel @Inject constructor(
    private val podcastServiceHandler: PodcastServiceHandler,
    savedStateHandle: SavedStateHandle
) : ViewModel(){
    var duration by savedStateHandle.saveable{ mutableStateOf(0L) }
    var progress by savedStateHandle.saveable{ mutableStateOf(0f) }
    var progressString by savedStateHandle.saveable{ mutableStateOf("00:00") }
    var isPlaying by savedStateHandle.saveable{ mutableStateOf(false) }
    var currentSelectedAudio by savedStateHandle.saveable{ mutableStateOf(podcastDummy) }
    var audioList by savedStateHandle.saveable { mutableStateOf(listOf<Episode>()) }

    private val _uiState: MutableStateFlow<UIState> = MutableStateFlow(UIState.Initial)
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    init {
        loadAudioData()
    }

    init {
        viewModelScope.launch {
            podcastServiceHandler.audioState.collectLatest { mediaState ->
                when (mediaState) {
                    PodcastAudioState.Initial -> _uiState.value = UIState.Initial
                    is PodcastAudioState.Buffering -> calculateProgressValue(mediaState.progress)
                    is PodcastAudioState.Playing -> isPlaying = mediaState.isPlaying
                    is PodcastAudioState.Progress -> calculateProgressValue(mediaState.progress)
                    is PodcastAudioState.CurrentPlaying -> {
                        currentSelectedAudio = audioList[mediaState.mediaItemIndex]
                    }

                    is PodcastAudioState.Ready -> {
                        duration = mediaState.duration
                        _uiState.value = UIState.Ready
                    }

                }
            }
        }
    }


    private fun loadAudioData(){
        viewModelScope.launch {
            val audio = DummyData.podcastEpisodesData
            audioList = audio
            setMediaItems()
        }
    }


    private fun setMediaItems(){
        audioList.map { audio ->
            MediaItem.Builder()
                .setUri(audio.uri)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setDisplayTitle(audio.title)
                        .setSubtitle(audio.description)
                        .build()
                )
                .build()
        }
            .also {
            podcastServiceHandler.setMediaItemList(it)
        }
    }

    private fun calculateProgressValue(currentProgress: Long) {
        progress =
            if (currentProgress > 0) ((currentProgress.toFloat() / duration.toFloat()) * 100f)
            else 0f
        progressString = formatDuration(currentProgress)
    }

    fun onUiEvents(uiEvents: UIEvents) = viewModelScope.launch {
        when (uiEvents) {
            UIEvents.Backward -> podcastServiceHandler.onPlayerEvents(PlayerEvent.Backward)
            UIEvents.Forward -> podcastServiceHandler.onPlayerEvents(PlayerEvent.Forward)
            UIEvents.SeekToNext -> podcastServiceHandler.onPlayerEvents(PlayerEvent.SeekToNext)
            is UIEvents.PlayPause -> {
                podcastServiceHandler.onPlayerEvents(
                    PlayerEvent.PlayPause
                )
            }

            is UIEvents.SeekTo -> {
                podcastServiceHandler.onPlayerEvents(
                    PlayerEvent.SeekTo,
                    seekPosition = ((duration * uiEvents.position) / 100f).toLong()
                )
            }

            is UIEvents.SelectedAudioChange -> {
                podcastServiceHandler.onPlayerEvents(
                    PlayerEvent.SelectedAudioChange,
                    selectedAudioIndex = uiEvents.index
                )
            }

            is UIEvents.UpdateProgress -> {
                podcastServiceHandler.onPlayerEvents(
                    PlayerEvent.UpdateProgress(
                        uiEvents.newProgress
                    )
                )
                progress = uiEvents.newProgress
            }
        }
    }

    fun formatDuration(duration: Long): String {
        val minute = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
        val seconds = (minute) - minute * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES)
        return String.format("%02d:%02d", minute, seconds)
    }

    override fun onCleared() {
        viewModelScope.launch {
            podcastServiceHandler.onPlayerEvents(PlayerEvent.Stop)
        }
        super.onCleared()
    }


}


sealed class UIEvents{
    object PlayPause : UIEvents()
    data class SelectedAudioChange(val index: Int) : UIEvents()
    data class SeekTo(val position: Float) : UIEvents()
    object SeekToNext : UIEvents()
    object Backward : UIEvents()
    object Forward : UIEvents()
    data class UpdateProgress(val newProgress: Float) : UIEvents()
}

sealed class UIState{
    object Initial : UIState()
    object Ready : UIState()
}