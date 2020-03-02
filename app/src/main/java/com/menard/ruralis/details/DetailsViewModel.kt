package com.menard.ruralis.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.menard.ruralis.add_places.PlaceDetailed
import com.menard.ruralis.data.FirestoreDataRepository
import com.menard.ruralis.data.GoogleApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailsViewModel(private val googleApiRepository: GoogleApiRepository, private val firestoreDataRepository: FirestoreDataRepository): ViewModel() {

    val placeLiveData = MutableLiveData<PlaceDetailed>()

    private fun getDetailsById(place_id: String, fields: String, key: String){
        viewModelScope.launch(Dispatchers.IO) {
            val place = googleApiRepository.getDetails(place_id, fields, key)
            withContext(Dispatchers.Main) {
                placeLiveData.value = place
            }
        }
    }

    private fun getPlaceFromFirestoreById(id: String){
        viewModelScope.launch(Dispatchers.IO) {
            val place = firestoreDataRepository.getPlaceFromFirestoreById(id)
            withContext(Dispatchers.Main){
                placeLiveData.value = place
            }
        }
    }

    fun getPlaceAccordingItsOrigin(fromRuralis: Boolean, place_id: String, fields: String, key: String) {
        if(fromRuralis){
            getDetailsById(place_id, fields, key)
        }else {
            getPlaceFromFirestoreById(place_id)
        }
    }
}