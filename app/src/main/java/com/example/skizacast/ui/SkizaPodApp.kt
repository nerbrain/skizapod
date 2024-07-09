package com.example.skizacast.ui

import android.annotation.SuppressLint
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.skizacast.R
import com.example.skizacast.ui.screens.HomeScreen
import com.example.skizacast.ui.screens.SelectedPodcastScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkizaPodApp(){
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold (
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { SkizaPodTopAppBar(scrollBehavior = scrollBehavior) }
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