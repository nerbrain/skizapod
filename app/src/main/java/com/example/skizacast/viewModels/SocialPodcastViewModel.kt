package com.example.skizacast.viewModels

import androidx.lifecycle.ViewModel
import com.example.skizacast.data.model.DummyData
import com.example.skizacast.data.model.Podcast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SocialPodcastViewModel: ViewModel() {
    private val _podcast = MutableStateFlow<List<Podcast>>(DummyData.top_podcasts_social_culture)
    val podcast: StateFlow<List<Podcast>> get() = _podcast
}