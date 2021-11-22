package com.shutterfly.pixabaygallery.repositories

import android.content.Context
import com.shutterfly.pixabaygallery.database.FavoritesImageData
import com.shutterfly.pixabaygallery.database.FavoritesImageInstance

class DBRepositoryImpl(context: Context): FavoritesImageDao {

    private val db: FavoritesImageInstance by lazy {
        FavoritesImageInstance.invoke(context)
    }

    override suspend fun insertAll(data: FavoritesImageData) = db.favoriteDao().insertAll(data)

    override suspend fun delete(id: FavoritesImageData) = db.favoriteDao().delete(id)

    override suspend fun isRowIsExist(id: Int): Boolean {
        return db.favoriteDao().isRowIsExist(id)
    }

}