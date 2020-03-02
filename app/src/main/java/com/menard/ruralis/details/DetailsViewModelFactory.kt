package com.menard.ruralis.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.menard.ruralis.data.FirestoreDataRepository
import com.menard.ruralis.data.GoogleApiRepository
import java.lang.IllegalArgumentException

class DetailsViewModelFactory(private val googleApiRepository: GoogleApiRepository, private val firestoreDataRepository: FirestoreDataRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailsViewModel::class.java)){
            return DetailsViewModel(
                googleApiRepository, firestoreDataRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}