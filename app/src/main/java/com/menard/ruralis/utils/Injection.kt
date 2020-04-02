package com.menard.ruralis.utils

import android.content.Context
import com.menard.ruralis.data.FavoritesDataRepository
import com.menard.ruralis.data.FirestoreDataRepository
import com.menard.ruralis.data.KnowsItRepository
import com.menard.ruralis.data.GoogleApiRepository
import com.menard.ruralis.database.FavoritesDatabase

class Injection {

    companion object {

        fun provideViewModelFactory(context: Context): ViewModelFactory {
            val textSearchRepository = GoogleApiRepository()
            val firestoreDataRepository = FirestoreDataRepository()
            val knowsItRepository = KnowsItRepository()
            val favoritesDataRepository = FavoritesDataRepository(FavoritesDatabase.getInstance(context).favoritesDao())
            return ViewModelFactory(
                favoritesDataRepository, textSearchRepository, firestoreDataRepository, knowsItRepository
            )
        }

    }
}