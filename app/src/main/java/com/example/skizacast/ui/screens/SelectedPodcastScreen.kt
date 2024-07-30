package com.example.skizacast.ui.screens

import android.content.Intent
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.skizacast.R
import com.example.skizacast.data.model.DummyData
import com.example.skizacast.data.model.Episode
import com.example.skizacast.player.service.PodcastService
import com.example.skizacast.viewModels.PodcastEpisodesViewModel

@UnstableApi
@Composable
fun SelectedPodcastScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    podcastEpisodesViewModel: PodcastEpisodesViewModel = hiltViewModel(),
) {

    val podcastEpisodes = podcastEpisodesViewModel.episode.collectAsState()

    val image = DummyData.top_podcasts[1].imageUrl
    val description = DummyData.top_podcasts[1].description

//    val sessionToken = SessionToken(context, ComponentName(context,PodcastService::class.java))
//    val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()


    Column (
        modifier = Modifier
            .padding(contentPadding)
            .fillMaxWidth()
            .fillMaxHeight()
    ){
        AsyncImage(model = ImageRequest.Builder(context = LocalContext.current)
            .data(image)
            .crossfade(true)
            .build(),
            contentDescription = "99Pi",
            placeholder = painterResource(R.drawable.loading_img),
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp))

        Text(text = description, textAlign = TextAlign.Left)

        Spacer(modifier = Modifier.padding(8.dp))

        PodcastEpisodes(episodes = podcastEpisodes.value, modifier = modifier)
    }
}

@Composable
fun PodcastEpisodes(
    episodes: List<Episode>,
    modifier: Modifier
){
    Column {
        LazyColumn {
            items(episodes){ episode ->
                EpisodeCard(
                    image = episode.image,
                    modifier = modifier,
                    episodeTitle = episode.title,
                    episodeDuration = episode.duration
                )
            }
        }
    }
}

@Composable
fun EpisodeCard(
    image: String,
    modifier: Modifier,
    episodeTitle: String,
    episodeDuration: String,
//    podcastPlayerViewModel: PodcastPlayerViewModel = hiltViewModel()
){
    val context = LocalContext.current
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ){
        AsyncImage(model = ImageRequest.Builder(context = LocalContext.current)
            .data(image)
            .crossfade(true)
            .build(),
            contentDescription = "99Pi",
            placeholder = painterResource(R.drawable.loading_img),
            modifier = modifier
                .width(60.dp)
                .height(60.dp))

        Spacer(modifier = Modifier.padding(10.dp))

        Text(text = episodeTitle, modifier = Modifier.width(150.dp))
        Spacer(modifier = Modifier.padding(10.dp))
        Text(text = episodeDuration, modifier = Modifier.width(60.dp), color = Color.DarkGray)
        Button(onClick = {
//            podcastPlayerViewModel.apply {
//                startPodcast(context = context)
//            }
            val intent = Intent(context,PodcastService::class.java)
            context.startService(intent)
        }) {
            Image(
                painter = painterResource(R.drawable.baseline_play_circle_outline_24),
                contentDescription = "Play Button"
            )
        }

    }
}

@UnstableApi
@Composable
fun Player(){


}


@OptIn(UnstableApi::class)
@Preview(showBackground = true)
@Composable
fun SelectedPodcastScreenPreview(){
    SelectedPodcastScreen(

    )
}

@Preview(showBackground = true)
@Composable
fun EpisodeCardPreview(){
    EpisodeCard(
        image = "https://image.simplecastcdn.com/images/00c81e60-45f9-4643-9fed-2184b2b6a3d3/5fbdc9d4-22ab-4b3a-a2bd-72777b15c30c/3000x3000/stitcher-cover-99percentinvisible-3000x3000-r2021-final.jpg",
        modifier = Modifier,
        episodeTitle = "Experiences and stories",
        episodeDuration = "20:21"
    )
}
