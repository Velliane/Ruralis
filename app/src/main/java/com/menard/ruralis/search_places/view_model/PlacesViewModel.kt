package com.menard.ruralis.search_places.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.menard.ruralis.add_places.FirestoreDataRepository
import com.menard.ruralis.add_places.Place
import com.menard.ruralis.search_places.textsearch_model.TextSearch
import com.menard.ruralis.search_places.view_model.TextSearchRepository
import kotlinx.coroutines.launch

class PlacesViewModel(private val textSearchRepository: TextSearchRepository, private val firestoreDataRepository: FirestoreDataRepository): ViewModel() {

    var listData = MutableLiveData<ArrayList<Place>>()

    fun getTextSearch(location: String, radius: String, query: String, key: String): LiveData<TextSearch> {
        return textSearchRepository.getTextSearch(location, radius, query, key)
    }

    fun getAllPlacesFromFirestore(){
        viewModelScope.launch {
            listData.value = firestoreDataRepository.getAllPlacesFromFirestore()
        }
    }

}