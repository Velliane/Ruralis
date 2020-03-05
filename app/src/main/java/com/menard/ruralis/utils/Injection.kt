package com.menard.ruralis.utils

import com.menard.ruralis.data.FirestoreDataRepository
import com.menard.ruralis.data.KnowsItRepository
import com.menard.ruralis.data.GoogleApiRepository

class Injection {

    companion object {

        fun provideViewModelFactory(): ViewModelFactory {
            val textSearchRepository = GoogleApiRepository()
            val firestoreDataRepository = FirestoreDataRepository()
            val knowsItRepository = KnowsItRepository()
            return ViewModelFactory(
                textSearchRepository, firestoreDataRepository, knowsItRepository
            )
        }

    }
}