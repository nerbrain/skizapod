package com.example.skizacast.viewModels

import androidx.lifecycle.ViewModel
import com.example.skizacast.data.model.DummyData
import com.example.skizacast.data.model.Episode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PodcastEpisodesViewModel: ViewModel() {
    private val _episode = MutableStateFlow<List<Episode>>(DummyData.podcastEpisodesData)
    val episode: StateFlow<List<Episode>> get() = _episode
}