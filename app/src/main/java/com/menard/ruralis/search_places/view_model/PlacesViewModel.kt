package com.menard.ruralis.search_places.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.menard.ruralis.add_places.FirestoreDataRepository
import com.menard.ruralis.add_places.Place
import com.menard.ruralis.search_places.details_model.ResultDetails
import kotlinx.coroutines.launch

class PlacesViewModel(private val googleApiRepository: GoogleApiRepository, private val firestoreDataRepository: FirestoreDataRepository): ViewModel() {


    val place = MutableLiveData<Place>()

    fun getTextSearch(location: String, radius: String, query: String, key: String): LiveData<ArrayList<Place>> {
        return googleApiRepository.getTextSearch(location, radius, query, key)
    }

    fun getDetailsById(place_id: String, fields: String, key: String): LiveData<Place> {
        return googleApiRepository.getDetails(place_id, fields, key)
    }

    fun getAllPlacesFromFirestore(): LiveData<ArrayList<Place>>{
        val listData = MutableLiveData<ArrayList<Place>>()
        viewModelScope.launch {
            listData.value = firestoreDataRepository.getAllPlacesFromFirestore()
        }
        return listData
    }

    fun getPlaceFromFirestoreById(id: String){
        viewModelScope.launch {
            place.value = firestoreDataRepository.getPlaceFromFirestoreById(id)
        }
    }

}