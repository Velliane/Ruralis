package com.menard.ruralis.utils

import android.content.Context
import com.menard.ruralis.add_places.geocode_model.GeocodeRepository
import com.menard.ruralis.data.*
import com.menard.ruralis.database.FavoritesDatabase

class Injection {

    companion object {

        fun provideViewModelFactory(context: Context): ViewModelFactory {
            val textSearchRepository = GoogleApiRepository()
            val firestoreDataRepository = FirestoreDataRepository()
            val knowsItRepository = KnowsItRepository()
            val favoritesDataRepository = FavoritesDataRepository(FavoritesDatabase.getInstance(context).favoritesDao())
            val geocodeRepository = GeocodeRepository()
            val connectivityRepository = ConnectivityRepository(context)
            val highScoreRepository = HighScoreRepository(FavoritesDatabase.getInstance(context).highScoreDao())
            return ViewModelFactory(
                context, highScoreRepository, connectivityRepository, geocodeRepository, favoritesDataRepository, textSearchRepository, firestoreDataRepository, knowsItRepository
            )
        }

    }
}