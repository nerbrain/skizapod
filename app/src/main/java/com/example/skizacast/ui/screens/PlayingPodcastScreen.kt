package com.example.skizacast.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.skizacast.viewModels.PodcastPlayerViewModel
import com.example.skizacast.viewModels.UIEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import java.nio.file.WatchEvent

@Composable
fun PlayingPodcastScreen(
    podcastPlayerViewModel: PodcastPlayerViewModel = viewModel()
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .background(Color.Gray)
                .clip(RoundedCornerShape(40.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Playback controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = { /* Previous */ }) {
                Icon(Icons.Filled.SkipPrevious, contentDescription = "Previous")
            }
            IconButton(onClick = { /* Play/Pause */ }) {
                Icon(Icons.Filled.PlayArrow, contentDescription = "Play/Pause")
            }
            IconButton(onClick = { /* Next */ }) {
                Icon(Icons.Filled.SkipNext, contentDescription = "Next")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Slider(
            value = podcastPlayerViewModel.progress,
            onValueChange = { podcastPlayerViewModel.onUiEvents(UIEvents.SeekTo(it)) },
            valueRange = 0f..100f,
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewPlayingPodcastScreen(){
    PlayingPodcastScreen()
}