package com.example.skizacast.viewModels

import androidx.lifecycle.ViewModel
import com.example.skizacast.data.model.DummyData
import com.example.skizacast.data.model.Podcast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TopPodcastViewModel: ViewModel(){
    private val _podcasts = MutableStateFlow<List<Podcast>>(DummyData.top_podcasts)
    val podcasts: StateFlow<List<Podcast>> get() = _podcasts
}