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