package com.menard.ruralis.search_places

import androidx.lifecycle.*
import com.menard.ruralis.data.FirestoreDataRepository
import com.menard.ruralis.add_places.PlaceDetailed
import com.menard.ruralis.data.GoogleApiRepository
import com.menard.ruralis.login.UserHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val googleApiRepository: GoogleApiRepository, private val firestoreDataRepository: FirestoreDataRepository): ViewModel() {

    val placeListLiveData = MutableLiveData<List<PlaceForList>>()
    val placeTextSearchListLiveData = MutableLiveData<List<PlaceForList>>()

    private val userHelper =  UserHelper()

    private val liveDataMerger = MediatorLiveData<List<PlaceForList>>()
    val allPlaceLiveData: LiveData<List<PlaceForList>> = liveDataMerger


    fun getTextSearch(location: String, radius: String, query: String, key: String){
        viewModelScope.launch(Dispatchers.IO) {
            val list = googleApiRepository.getTextSearch(location, radius, query, key)
            withContext(Dispatchers.Main){
                placeTextSearchListLiveData.value = list
            }
        }
    }


    fun getAllPlacesFromFirestore(){
        viewModelScope.launch(Dispatchers.IO) {
            val list = firestoreDataRepository.getAllPlacesFromFirestore()
            withContext(Dispatchers.Main){
                placeListLiveData.value = list
            }
        }
    }


    fun getAllPlaces(location: String, radius: String, query: String, key: String){
        getTextSearch(location, radius, query, key)
        getAllPlacesFromFirestore()
        liveDataMerger.addSource(placeListLiveData){
            if(it != null){
                liveDataMerger.value = mergeList(it, placeTextSearchListLiveData.value!!)
            }
        }

        liveDataMerger.addSource(placeTextSearchListLiveData){
            if(it != null){
                liveDataMerger.value = mergeList(placeListLiveData.value!!, it)
            }
        }
    }

    private fun mergeList(placeDetailedList: List<PlaceForList>, placeTextSearchList: List<PlaceForList>): List<PlaceForList> {
        val listPlaces  = placeDetailedList + placeTextSearchList
        return listPlaces.distinct()
    }
}