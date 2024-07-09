package com.example.skizacast.data.model

data class Podcast(
    val id: String,
    val title: String,
    val description: String,
    val author: String,
    val imageUrl: String,
    val episodeCount: Int
)
