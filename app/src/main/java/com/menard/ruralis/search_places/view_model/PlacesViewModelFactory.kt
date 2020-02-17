package com.menard.ruralis.search_places.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.menard.ruralis.add_places.FirestoreDataRepository
import java.lang.IllegalArgumentException

class PlacesViewModelFactory(private val textSearchRepository: TextSearchRepository, private val firestoreDataRepository: FirestoreDataRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlacesViewModel::class.java)){
            return PlacesViewModel(
                textSearchRepository, firestoreDataRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }

}