package com.menard.ruralis.search_places

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.menard.ruralis.data.FirestoreDataRepository
import com.menard.ruralis.data.GoogleApiRepository
import java.lang.IllegalArgumentException

class MainViewModelFactory(private val googleApiRepository: GoogleApiRepository, private val firestoreDataRepository: FirestoreDataRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(
                googleApiRepository, firestoreDataRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }

}