package com.shutterfly.pixabaygallery.repositories

import androidx.room.*
import com.shutterfly.pixabaygallery.database.FavoritesImageData

@Dao
interface FavoritesImageDao {

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertAll(data: FavoritesImageData)

   @Delete
   suspend fun delete(id: FavoritesImageData)

   @Query("SELECT EXISTS(SELECT * FROM favorites_images WHERE id = :id)")
   suspend fun isRowIsExist(id : Int) : Boolean
}
