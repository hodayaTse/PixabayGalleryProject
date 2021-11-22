package com.shutterfly.pixabaygallery.network

import com.shutterfly.pixabaygallery.models.GalleryData
import retrofit2.http.GET
import retrofit2.http.Query

private const val PIXABAY_SERVICE_KEY = "12175339-7048b7105116d7fa1da74220c"

interface PixabayService {

    @GET("?key=$PIXABAY_SERVICE_KEY&image_type=photo")
    suspend fun loadImagesByKey(@Query("per_page") loadSize: Int, @Query("page") page: Int, @Query("q") keyword: String): GalleryData
}