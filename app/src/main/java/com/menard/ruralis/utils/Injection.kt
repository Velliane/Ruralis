package com.menard.ruralis.utils

import com.menard.ruralis.search_places.TextSearchRepository
import com.menard.ruralis.search_places.TextSearchViewModelFactory

class Injection {

    companion object {

        fun provideTextSearchViewModelFactory(): TextSearchViewModelFactory {
            val textSearchRepository =
                TextSearchRepository()
            return TextSearchViewModelFactory(
                textSearchRepository
            )
        }
    }
}