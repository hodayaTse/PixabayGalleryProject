package com.shutterfly.pixabaygallery.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.shutterfly.pixabaygallery.repositories.FavoritesImageDao

@Database(
    entities = [FavoritesImageData::class],
    version = 1
)

abstract class FavoritesImageInstance : RoomDatabase(){
    abstract fun favoriteDao(): FavoritesImageDao

    companion object {
        @Volatile private var instance: FavoritesImageInstance? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            FavoritesImageInstance::class.java, "favorites-images-list.db")
            .build()
    }
}
