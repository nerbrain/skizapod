package com.example.skizacast.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.skizacast.R
import com.example.skizacast.data.model.Podcast
import com.example.skizacast.viewModels.BusinessPodcastViewModel
import com.example.skizacast.viewModels.SocialPodcastViewModel
import com.example.skizacast.viewModels.TopPodcastViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    topPodcastViewModel: TopPodcastViewModel = hiltViewModel(),
    socialPodcastViewModel: SocialPodcastViewModel = hiltViewModel(),
    businessPodcastViewModel: BusinessPodcastViewModel = hiltViewModel()
){
    val topPodcastList = topPodcastViewModel.podcasts.collectAsState()
    val socialPodcastList = socialPodcastViewModel.podcast.collectAsState()
    val businessPodcastList = businessPodcastViewModel.podcast.collectAsState()

    Column (
        modifier = Modifier.padding(contentPadding)
    ){
        Text(
            text = stringResource(R.string.top_podcasts),
            style = MaterialTheme.typography.titleLarge,
            color = Color.Black,
            textAlign = TextAlign.Left,
            modifier = Modifier.padding(4.dp)
        )
        TopPodcastsHorizontalScroll(podcasts = topPodcastList.value, modifier = modifier)

        Spacer(modifier = Modifier.padding(8.dp))

        Text(
            text = stringResource(R.string.popular_social_culture),
            style = MaterialTheme.typography.titleLarge,
            color = Color.Black,
            textAlign = TextAlign.Left,
            modifier = Modifier.padding(4.dp)
        )

        TopPodcastsSocialCulture(podcasts = socialPodcastList.value, modifier = modifier)

        Spacer(modifier = Modifier.padding(8.dp))

        Text(
            text = stringResource(R.string.popular_business),
            style = MaterialTheme.typography.titleLarge,
            color = Color.Black,
            textAlign = TextAlign.Left,
            modifier = Modifier.padding(4.dp)
        )

        TopPodcastsBusiness(podcasts = businessPodcastList.value, modifier = modifier)

    }
}

@Composable
fun TopPodcastsHorizontalScroll(
    podcasts: List<Podcast>,
    modifier: Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
){
    LazyRow(
        modifier = modifier.padding(horizontal = 0.dp),
        contentPadding = contentPadding
    ) {
        items(podcasts) { podcast ->
            PodcastCard(
                image = podcast.imageUrl,
                title = podcast.title,
                modifier = modifier
                    .padding(4.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun TopPodcastsSocialCulture(
    podcasts: List<Podcast>,
    modifier: Modifier
){
    LazyRow {
        items(podcasts){podcast ->
            PodcastCard(
                image = podcast.imageUrl,
                title = podcast.title,
                modifier = modifier
                    .padding(4.dp)
                    .fillMaxWidth())
        }
    }
}

@Composable
fun TopPodcastsBusiness(
    podcasts: List<Podcast>,
    modifier: Modifier
){
    LazyRow {
        items(podcasts){podcast ->
            PodcastCard(
                image = podcast.imageUrl,
                title = podcast.title,
                modifier = modifier
                    .padding(4.dp)
                    .fillMaxWidth()
            )
        }
    }
}



@Composable
fun PodcastCard(image: String, title: String, modifier: Modifier){
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = modifier
        ) {
            AsyncImage(model = ImageRequest.Builder(context = LocalContext.current)
                .data(image)
                .crossfade(true)
                .build(),
                contentDescription = "99Pi",
                placeholder = painterResource(R.drawable.loading_img),
                modifier = modifier
                    .width(120.dp)
                    .height(120.dp))

            Text(text = title, textAlign = TextAlign.Center, modifier = modifier.width(120.dp))
        }
    }
}

@Preview
@Composable
fun CardPreview(
    topPodcastViewModel: TopPodcastViewModel = hiltViewModel()
){
    val podcastList = topPodcastViewModel.podcasts.collectAsState()
    val podcasts = podcastList.value
    PodcastCard(image = podcasts[0].imageUrl, title = podcasts[0].title, modifier = Modifier)
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    HomeScreen()
}