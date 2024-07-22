package com.example.skizacast.data.model

import android.net.Uri

data class Episode (
    val id: String,
    val title: String,
    val description: String,
    val duration: String,
    val image: String,
    val uri: String
)