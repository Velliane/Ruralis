package com.menard.ruralis.viewmodel.injections

import android.content.Context
import com.menard.ruralis.repository.TextSearchRepository
import com.menard.ruralis.viewmodel.TextSearchViewModel

class Injection {

    companion object {

        fun provideTextSearchViewModelFactory(): TextSearchViewModelFactory {
            val textSearchRepository = TextSearchRepository()
            return TextSearchViewModelFactory(textSearchRepository)
        }
    }
}