package com.menard.ruralis.search_places.map

import androidx.lifecycle.*
import com.menard.ruralis.data.FirestoreDataRepository
import com.menard.ruralis.data.GoogleApiRepository
import com.menard.ruralis.search_places.PlaceForList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapViewModel(private val googleApiRepository: GoogleApiRepository, private val firestoreDataRepository: FirestoreDataRepository): ViewModel() {

    private val placeListLiveData = MutableLiveData<List<PlaceForList>>()
    private val placeTextSearchListLiveData = MutableLiveData<List<PlaceForList>>()

    private val liveDataMerger = MediatorLiveData<List<PlaceForList>>()
    val allPlaceLiveData: LiveData<List<PlaceForList>> = liveDataMerger


    private fun getTextSearch(location: String, radius: String, query: String, key: String){
        viewModelScope.launch(Dispatchers.IO) {
            val list = googleApiRepository.getTextSearch(location, radius, query, key)
            withContext(Dispatchers.Main){
                placeTextSearchListLiveData.value = list
            }
        }
    }


    private fun getAllPlacesFromFirestore(){
        viewModelScope.launch(Dispatchers.IO) {
            val list = firestoreDataRepository.getAllPlacesFromFirestore()
            withContext(Dispatchers.Main){
                placeListLiveData.value = list
            }
        }
    }


    fun getAllPlaces(location: String, radius: String, query: String, key: String){
        getAllPlacesFromFirestore()
        getTextSearch(location, radius, query, key)
        liveDataMerger.addSource(placeListLiveData){
            mergeList(it, placeTextSearchListLiveData.value)
        }
        liveDataMerger.addSource(placeTextSearchListLiveData){
            mergeList(placeListLiveData.value, it)
        }
    }

    private fun mergeList(placeDetailedList: List<PlaceForList>?, placeTextSearchList: List<PlaceForList>?){
        if(placeDetailedList == null || placeTextSearchList == null){
            return
        }
        val listPlaces  = placeDetailedList + placeTextSearchList
        liveDataMerger.value = listPlaces.distinct()
    }
}