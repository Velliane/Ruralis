package com.menard.ruralis.search_places

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.menard.ruralis.search_places.textsearch_model.TextSearch

class TextSearchViewModel(private val textSearchRepository: TextSearchRepository): ViewModel() {

    fun getTextSearch(location: String, radius: String, query: String, key: String): MutableLiveData<TextSearch> {
        return textSearchRepository.getTextSearch(location, radius, query, key)
    }
}