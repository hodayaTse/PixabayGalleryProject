package com.shutterfly.pixabaygallery.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.shutterfly.pixabaygallery.network.GalleryPagingSource

class GalleryRepository {

    private val defaultPagingConfig by lazy { PagingConfig(pageSize = 30, enablePlaceholders = false) }

    fun searchImages(keyword: String) = Pager(config = defaultPagingConfig) {
        GalleryPagingSource(keyword)
    }.liveData
}