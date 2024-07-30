package com.example.skizacast.ui

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.UnstableApi
import com.example.skizacast.R
import com.example.skizacast.ui.components.PodcastController
import com.example.skizacast.ui.screens.HomeScreen
import com.example.skizacast.ui.screens.SelectedPodcastScreen
import com.example.skizacast.viewModels.PodcastPlayerViewModel

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkizaPodApp(podcastPlayerViewModel: PodcastPlayerViewModel = viewModel()){
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val isPlaying = podcastPlayerViewModel.isPlaying.collectAsState()
    Scaffold (
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { SkizaPodTopAppBar(scrollBehavior = scrollBehavior) },
        {
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                PodcastController(
                    image = "https://image.simplecastcdn.com/images/00c81e60-45f9-4643-9fed-2184b2b6a3d3/5fbdc9d4-22ab-4b3a-a2bd-72777b15c30c/3000x3000/stitcher-cover-99percentinvisible-3000x3000-r2021-final.jpg",
                    episodeTitle = "Episode 1"
                )
            }
        }
    ){
        Surface (
            modifier = Modifier.fillMaxSize()
        ){
//            HomeScreen(
//                contentPadding = it
//            )

            SelectedPodcastScreen(
                contentPadding = it
            )


        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkizaPodTopAppBar(scrollBehavior: TopAppBarScrollBehavior, modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineSmall,
            )
        },
        modifier = modifier
    )
}