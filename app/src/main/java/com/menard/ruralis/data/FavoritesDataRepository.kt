package com.menard.ruralis.data

import com.menard.ruralis.database.FavoritesDao
import com.menard.ruralis.details.Favorite

class FavoritesDataRepository(private val favoritesDao: FavoritesDao){

    suspend fun getAllFavorites(): List<Favorite> {
        return favoritesDao.getAllFavorites()
    }

    suspend fun addFavorites(favorite: Favorite): Long {
        return favoritesDao.addFavorite(favorite)
    }

    suspend fun deleteFavorite(id: String): Int {
        return favoritesDao.deleteFavorite(id)
    }

    suspend fun getFavoriteById(id: String): Favorite? {
        return favoritesDao.getFavoriteById(id)
    }
}