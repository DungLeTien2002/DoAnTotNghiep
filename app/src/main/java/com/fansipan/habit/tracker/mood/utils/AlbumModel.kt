package com.fansipan.habit.tracker.mood.utils

data class AlbumModel(
    val coverUri: String?,
    val name: String,
    var albumPhotos: MutableList<ImageModel>? = mutableListOf()
)
