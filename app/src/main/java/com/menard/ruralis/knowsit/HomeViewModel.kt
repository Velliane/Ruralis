package com.menard.ruralis.knowsit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.menard.ruralis.data.FavoritesDataRepository
import com.menard.ruralis.data.FirestoreDataRepository
import com.menard.ruralis.data.GoogleApiRepository
import com.menard.ruralis.data.KnowsItRepository
import com.menard.ruralis.details.Favorite
import com.menard.ruralis.search_places.PlaceForList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(private val favoritesDataRepository: FavoritesDataRepository, private val knowsItRepository: KnowsItRepository, private val googleApiRepository: GoogleApiRepository, private val firestoreDataRepository: FirestoreDataRepository): ViewModel() {

    val randomKnowsIt = MutableLiveData<KnowsIt>()
    private val listFavoritesLiveData = MutableLiveData<List<Favorite>>()

    fun getRandomKnowsIt(){
        viewModelScope.launch {
            randomKnowsIt.value = knowsItRepository.getRandomKnowsIt()
        }
    }

//    private fun getPlaceFromFirestoreById(id: String){
//        viewModelScope.launch(Dispatchers.IO) {
//            val place = firestoreDataRepository.getPlaceForListById(id)
//            withContext(Dispatchers.Main){
//                listFavoritesLiveData.value = place
//            }
//        }
//    }
//
//    fun getPlaceFavoriteAccordingItsOrigin(fromRuralis: Boolean, place_id: String, fields: String, key: String): LiveData<PlaceForList> {
//        if(!fromRuralis){
//            getPlaceFromRoomById(place_id, fields, key)
//        }else {
//            getPlaceFromFirestoreById(place_id)
//        }
//        return listFavoritesLiveData
//    }
//
//    private fun getPlaceFromRoomById(placeId: String, fields: String, key: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val place = googleApiRepository.getPlaceFavorites(placeId, fields, key)
//            withContext(Dispatchers.Main) {
//                listFavoritesLiveData.value = place
//            }
//        }
//    }

    fun showAllFavorites(): LiveData<List<Favorite>>{
        viewModelScope.launch(Dispatchers.IO) {
            val list = favoritesDataRepository.getAllFavorites()
            withContext(Dispatchers.Main){
                listFavoritesLiveData.value = list
            }
        }
        return listFavoritesLiveData
    }
}