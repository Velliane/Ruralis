package com.menard.ruralis.utils

import com.menard.ruralis.data.FirestoreDataRepository
import com.menard.ruralis.knowsit.HomeViewModelFactory
import com.menard.ruralis.data.KnowsItRepository
import com.menard.ruralis.data.GoogleApiRepository
import com.menard.ruralis.details.DetailsViewModel
import com.menard.ruralis.details.DetailsViewModelFactory
import com.menard.ruralis.login.UserViewModelFactory
import com.menard.ruralis.search_places.MainViewModelFactory

class Injection {

    companion object {

        fun provideMainViewModelFactory(): MainViewModelFactory {
            val textSearchRepository = GoogleApiRepository()
            val firestoreDataRepository = FirestoreDataRepository()
            return MainViewModelFactory(
                textSearchRepository, firestoreDataRepository
            )
        }

        fun provideHomeViewModelFactory(): HomeViewModelFactory {
            val knowsItRepository = KnowsItRepository()
            return HomeViewModelFactory(knowsItRepository)
        }

        fun provideUserViewModelFactory(): UserViewModelFactory {
            return UserViewModelFactory()
        }

        fun provideDetailsViewModelFactory(): DetailsViewModelFactory {
            val textSearchRepository = GoogleApiRepository()
            val firestoreDataRepository = FirestoreDataRepository()
            return DetailsViewModelFactory(textSearchRepository, firestoreDataRepository)
        }
    }
}