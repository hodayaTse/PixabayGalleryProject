package com.shutterfly.pixabaygallery.models

import com.google.gson.annotations.SerializedName

data class GalleryData(
    @SerializedName("hits")
    val galleryItems: List<GalleryItem>
)

data class GalleryItem(
    val id: Int,
    var position: Int,
    @SerializedName("previewURL")
    val previewUrl: String,
    val likes: String,
    var isFavorite: Boolean = false
)