package com.menard.ruralis.details

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.menard.ruralis.add_places.PlaceDetailed
import com.menard.ruralis.data.FavoritesDataRepository
import com.menard.ruralis.data.FirestoreDataRepository
import com.menard.ruralis.data.GoogleApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailsViewModel(private val context: Context, private val favoritesDataRepository: FavoritesDataRepository, private val googleApiRepository: GoogleApiRepository, private val firestoreDataRepository: FirestoreDataRepository): ViewModel() {

    val placeLiveData = MutableLiveData<PlaceDetailed>()
    private val isFavoriteLiveData = MutableLiveData<Boolean>()

    private fun getDetailsById(place_id: String, fields: String, key: String){
        viewModelScope.launch(Dispatchers.IO) {
            val place = googleApiRepository.getDetails(place_id, fields, key, context)
            withContext(Dispatchers.Main) {
                placeLiveData.value = place
            }
        }
    }

    fun getPlaceFromFirestoreById(id: String?){
        viewModelScope.launch(Dispatchers.IO) {
            val place = firestoreDataRepository.getPlaceFromFirestoreById(id)
            withContext(Dispatchers.Main){
                placeLiveData.value = place
            }
        }
    }

    fun getPlaceAccordingItsOrigin(fromRuralis: Boolean, place_id: String, fields: String, key: String): LiveData<PlaceDetailed>{
        if(!fromRuralis){
            getDetailsById(place_id, fields, key)
        }else {
            getPlaceFromFirestoreById(place_id)
        }
        return placeLiveData
    }

    fun addToFavorites(placeId: String?, fromRuralis: Boolean, photoUri: String?, name: String) {
        val favorite = Favorite(placeId!!, name, photoUri, fromRuralis)
        viewModelScope.launch(Dispatchers.IO) {
            favoritesDataRepository.addFavorites(favorite)
        }
    }

    fun deleteFromFavorites(placeId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            favoritesDataRepository.deleteFavorite(placeId)
        }
    }

    fun checkIfAlreadyInFavorites(placeId: String): LiveData<Boolean> {
        viewModelScope.launch(Dispatchers.IO) {
            val favorite = favoritesDataRepository.getFavoriteById(placeId)
            withContext(Dispatchers.Main){
                isFavoriteLiveData.value = favorite != null
            }
        }
        return isFavoriteLiveData
    }
}