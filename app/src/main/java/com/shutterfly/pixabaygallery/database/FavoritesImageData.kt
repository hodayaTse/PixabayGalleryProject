package com.shutterfly.pixabaygallery.database

import androidx.room.*

@Entity(tableName = "favorites_images")
data class FavoritesImageData(
    @PrimaryKey val id: Int? = null)

