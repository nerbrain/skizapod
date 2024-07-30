package com.example.skizacast.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.skizacast.R
import com.example.skizacast.viewModels.PodcastPlayerViewModel

@Composable
fun PodcastController(
    image: String,
    episodeTitle: String,
    podcastPlayerViewModel: PodcastPlayerViewModel = hiltViewModel()
){

    val isPlaying = podcastPlayerViewModel.isPlaying.collectAsState()
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(60.dp)
        .background(color = Color.LightGray)){
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            AsyncImage(model = ImageRequest.Builder(context = LocalContext.current)
                .data(image)
                .crossfade(true)
                .build(),
                contentDescription = "99Pi",
                placeholder = painterResource(R.drawable.loading_img),
                modifier = Modifier
                    .width(60.dp)
                    .height(60.dp)
            )



            Text(text = episodeTitle)

            Spacer(modifier = Modifier.padding(10.dp))

            // Play/Pause button
            IconButton(onClick = {
                podcastPlayerViewModel.playPause()
            }) {
                Image(
                    if (isPlaying.value) painterResource(id = R.drawable.baseline_pause_24) else painterResource(id = R.drawable.baseline_play_arrow_24),
                    contentDescription = if (isPlaying.value) "Pause" else "Play"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlayerPrev(){
    PodcastController(image = "https://image.simplecastcdn.com/images/00c81e60-45f9-4643-9fed-2184b2b6a3d3/5fbdc9d4-22ab-4b3a-a2bd-72777b15c30c/3000x3000/stitcher-cover-99percentinvisible-3000x3000-r2021-final.jpg", episodeTitle = "Episode 1")
}