package com.menard.ruralis.search_places.map

import android.content.Context
import android.location.Location
import androidx.lifecycle.*
import com.menard.ruralis.data.FirestoreDataRepository
import com.menard.ruralis.data.GoogleApiRepository
import com.menard.ruralis.search_places.PlaceForList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapViewModel(private val context: Context, private val googleApiRepository: GoogleApiRepository, private val firestoreDataRepository: FirestoreDataRepository): ViewModel() {

    private val placeListLiveData = MutableLiveData<List<PlaceForList>>()
    private val placeTextSearchListLiveData = MutableLiveData<List<PlaceForList>>()

    private val liveDataMerger = MediatorLiveData<List<PlaceForList>>()
    val allPlaceLiveData: LiveData<List<PlaceForList>> = liveDataMerger


    private fun getTextSearch(location: String, radius: String, query: String, key: String){
        viewModelScope.launch(Dispatchers.IO) {
            val list = googleApiRepository.getTextSearch(context, location, radius, query, key)
            withContext(Dispatchers.Main){
                placeTextSearchListLiveData.value = list
            }
        }
    }


    private fun getAllPlacesFromFirestore(location: Location, radius: String){
        viewModelScope.launch(Dispatchers.IO) {
            val list = firestoreDataRepository.getAllPlacesFromFirestore(location, radius, context)
            withContext(Dispatchers.Main){
                placeListLiveData.value = list
            }
        }
    }


    fun getAllPlaces(location: String, radius: String, query: String, key: String, locationForFirestore: Location){
        getAllPlacesFromFirestore(locationForFirestore, radius)
        getTextSearch(location, radius, query, key)
        if(liveDataMerger.hasObservers()){
            liveDataMerger.removeSource(placeListLiveData)
            liveDataMerger.removeSource(placeTextSearchListLiveData)
        }
        liveDataMerger.addSource(placeListLiveData, Observer {
            mergeList(it, placeTextSearchListLiveData.value)
        })
        liveDataMerger.addSource(placeTextSearchListLiveData, Observer {
            mergeList(placeListLiveData.value, it)
        })
    }

    private fun mergeList(placeDetailedList: List<PlaceForList>?, placeTextSearchList: List<PlaceForList>?){
        if(placeDetailedList == null || placeTextSearchList == null){
            return
        }
        val listPlaces  = placeDetailedList + placeTextSearchList
        liveDataMerger.value = listPlaces.distinct()
    }

    fun refreshList(locationForFirestore: Location, radius: String, query: String, key: String, location: String){
        getAllPlacesFromFirestore(locationForFirestore, radius)
        getTextSearch(location, radius, query, key)
    }
}