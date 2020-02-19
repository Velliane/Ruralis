package com.menard.ruralis.utils

import com.menard.ruralis.add_places.FirestoreDataRepository
import com.menard.ruralis.knowsit.HomeViewModelFactory
import com.menard.ruralis.knowsit.KnowsItRepository
import com.menard.ruralis.search_places.view_model.GoogleApiRepository
import com.menard.ruralis.search_places.view_model.PlacesViewModelFactory

class Injection {

    companion object {

        fun providePlacesViewModelFactory(): PlacesViewModelFactory {
            val textSearchRepository =
                GoogleApiRepository()
            val firestoreDataRepository = FirestoreDataRepository()
            return PlacesViewModelFactory(
                textSearchRepository, firestoreDataRepository
            )
        }

        fun provideHomeViewModelFactory(): HomeViewModelFactory {
            val knowsItRepository = KnowsItRepository()
            return HomeViewModelFactory(knowsItRepository)
        }
    }
}