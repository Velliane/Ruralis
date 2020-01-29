package com.menard.ruralis.viewmodel.injections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.menard.ruralis.repository.TextSearchRepository
import com.menard.ruralis.viewmodel.TextSearchViewModel
import java.lang.IllegalArgumentException

class TextSearchViewModelFactory(private val textSearchRepository: TextSearchRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TextSearchViewModel::class.java)){
            return TextSearchViewModel(textSearchRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }

}