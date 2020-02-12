package com.menard.ruralis.search_places

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class TextSearchViewModelFactory(private val textSearchRepository: TextSearchRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TextSearchViewModel::class.java)){
            return TextSearchViewModel(
                textSearchRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }

}