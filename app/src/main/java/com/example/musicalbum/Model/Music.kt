package com.example.musicalbum.Model

data class Music (
    val id: Int,
    val title: String,
    val photos: List<Photo>?
)

data class Photo(
    val albumId: Int,
    val id: Int,
    val title: String,
    val url: String,
    val thumbnailUrl: String
)
