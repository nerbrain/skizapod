package com.example.skizacast.ui


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.skizacast.R
import com.example.skizacast.data.model.Episode
import com.example.skizacast.player.service.PodcastService
import com.example.skizacast.ui.components.BottomBar
import com.example.skizacast.ui.screens.HomeScreen
import com.example.skizacast.ui.screens.SelectedPodcastScreen
import com.example.skizacast.viewModels.PodcastPlayerViewModel
import com.example.skizacast.viewModels.UIEvents

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkizaPodApp(podcastPlayerViewModel: PodcastPlayerViewModel = viewModel()){
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val context = LocalContext.current



    Scaffold (
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { SkizaPodTopAppBar(scrollBehavior = scrollBehavior) },
        bottomBar = {
            BottomBar(
                progress = podcastPlayerViewModel.progress,
                onProgress = { podcastPlayerViewModel.onUiEvents(UIEvents.SeekTo(it)) },
                isAudioPlaying = podcastPlayerViewModel.isPlaying,
                onStart = { podcastPlayerViewModel.onUiEvents(UIEvents.PlayPause)},
                onNext = {podcastPlayerViewModel.onUiEvents(UIEvents.SeekToNext)}
            )
        }
    ){
        Surface (
            modifier = Modifier.fillMaxSize()
        ){
            SelectedPodcastScreen(
                contentPadding = it,
                onItemClick = {
                    play(it, context, podcastPlayerViewModel)
                }
            )
        }
    }
}

fun play(it: Episode, context: Context, podcastPlayerViewModel: PodcastPlayerViewModel){
    podcastPlayerViewModel.setMediaItem(it)
    podcastPlayerViewModel.onUiEvents(UIEvents.SelectedAudioChange(it.id.toInt()))
    val intent = Intent(context, PodcastService::class.java)
    context.startService(intent)
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

@Composable
fun SkizaApp(podcastPlayerViewModel: PodcastPlayerViewModel = viewModel(), contentPadding: PaddingValues){
    val navController = rememberNavController()
    val context = LocalContext.current
    NavHost(
        navController,
        startDestination = "pod"
    ){
        composable(route = "pod") {
            HomeScreen(contentPadding = contentPadding)
        }
        composable(route = "pod/{pod_id}"){
            SelectedPodcastScreen(
                contentPadding = contentPadding,
                onItemClick = {
                play(it, context, podcastPlayerViewModel)
            })
        }
    }
}