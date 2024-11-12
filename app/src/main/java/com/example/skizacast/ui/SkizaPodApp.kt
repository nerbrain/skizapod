package com.example.skizacast.ui


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.skizacast.R
import com.example.skizacast.data.model.Episode
import com.example.skizacast.player.service.PodcastService
import com.example.skizacast.ui.components.BottomBar
import com.example.skizacast.ui.screens.HomeScreen
import com.example.skizacast.ui.screens.PlayingPodcastScreen
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
    val navController = rememberNavController()

    Scaffold (
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { SkizaPodTopAppBar(scrollBehavior = scrollBehavior) },
        bottomBar = {
            if(podcastPlayerViewModel.isActive){
                BottomBar(
                    progress = podcastPlayerViewModel.progress,
                    onProgress = { podcastPlayerViewModel.onUiEvents(UIEvents.SeekTo(it)) },
                    isAudioPlaying = podcastPlayerViewModel.isPlaying,
                    onStart = { podcastPlayerViewModel.onUiEvents(UIEvents.PlayPause)},
                    onNext = {podcastPlayerViewModel.onUiEvents(UIEvents.SeekToNext)},
                    navController = navController
                )
            }

        }
    ){
        Surface (
            modifier = Modifier.fillMaxSize()
        ){
            SkizaApp(contentPadding = it, navController = navController)
//            if(showBottomSheet){
//                BottomSheetScaffold(
//                    sheetPeekHeight = 128.dp,
//                    scaffoldState = scaffoldState,
//                    sheetContent = {
//                        BottomSheetPlayer(
//                            progress = podcastPlayerViewModel.progress,
//                            onProgress = { podcastPlayerViewModel.onUiEvents(UIEvents.SeekTo(it)) },
//                            isAudioPlaying = podcastPlayerViewModel.isPlaying,
//                            onStart = { podcastPlayerViewModel.onUiEvents(UIEvents.PlayPause)},
//                            onNext = {podcastPlayerViewModel.onUiEvents(UIEvents.SeekToNext)},
//                            image = "https://image.simplecastcdn.com/images/00c81e60-45f9-4643-9fed-2184b2b6a3d3/5fbdc9d4-22ab-4b3a-a2bd-72777b15c30c/3000x3000/stitcher-cover-99percentinvisible-3000x3000-r2021-final.jpg"
//                        )
//                    }
//                ) { innerPadding ->
//                    SkizaApp(contentPadding = it)
//                }
//            }
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
fun SkizaApp(podcastPlayerViewModel: PodcastPlayerViewModel = viewModel(), contentPadding: PaddingValues,
             navController: NavController ){
    val context = LocalContext.current
    NavHost(
        navController as NavHostController,
        startDestination = "pod"
    ){
        composable(route = "pod") {
            HomeScreen(contentPadding = contentPadding){ id ->
                navController.navigate("pod/{id}")
            }
        }
        composable(route = "pod/{pod_id}"){
            SelectedPodcastScreen(
                contentPadding = contentPadding,
                onItemClick = {
                play(it, context, podcastPlayerViewModel)
            })
        }
        composable(route="player_screen"){
            PlayingPodcastScreen(podcastPlayerViewModel)

            //TODO Make it look better
        }
    }
}
