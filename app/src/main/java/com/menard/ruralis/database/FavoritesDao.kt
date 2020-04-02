package com.menard.ruralis.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.menard.ruralis.details.Favorite

@Dao
interface FavoritesDao {

    @Query("SELECT * FROM Favorite")
    suspend fun getAllFavorites(): List<Favorite>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: Favorite): Long

    @Query("DELETE FROM Favorite WHERE id = :favoriteId")
    suspend fun deleteFavorite(favoriteId: String): Int

    @Query("SELECT * FROM Favorite WHERE id = :favoriteId")
    suspend fun getFavoriteById(favoriteId: String): Favorite?
}