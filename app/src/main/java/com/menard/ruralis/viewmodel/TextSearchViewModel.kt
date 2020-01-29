package com.menard.ruralis.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.menard.ruralis.model.textsearch.TextSearch
import com.menard.ruralis.repository.TextSearchRepository
import java.util.concurrent.Executor

class TextSearchViewModel(private val textSearchRepository: TextSearchRepository): ViewModel() {

    fun getTextSearch(location: String, radius: String, query: String, key: String): MutableLiveData<TextSearch> {
        return textSearchRepository.getTextSearch(location, radius, query, key)
    }
}