package com.menard.ruralis.search_places.list

import android.content.Context
import android.location.Location
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.menard.ruralis.R
import com.menard.ruralis.data.FirestoreDataRepository
import com.menard.ruralis.data.GoogleApiRepository
import com.menard.ruralis.search_places.PlaceForList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class ListViewModel(private val context: Context, private val googleApiRepository: GoogleApiRepository, private val firestoreDataRepository: FirestoreDataRepository): ViewModel() {

    val placeListLiveData = MutableLiveData<List<PlaceForList>>()
    val placeTextSearchListLiveData = MutableLiveData<List<PlaceForList>>()
    val filteredPlacesLiveData = MutableLiveData<List<PlaceForList>>()

    val liveDataMerger = MediatorLiveData<List<PlaceForList>>()
    val allPlaceLiveData: LiveData<List<PlaceForList>> = liveDataMerger


    fun getTextSearch(location: String, radius: String, query: String, key: String){
        viewModelScope.launch(Dispatchers.IO) {
            val list = googleApiRepository.getTextSearch(context, location, radius, query, key)
            withContext(Dispatchers.Main){
                placeTextSearchListLiveData.value = list
            }
        }
    }


    fun getAllPlacesFromFirestore(){
        viewModelScope.launch(Dispatchers.IO) {
            val list = firestoreDataRepository.getAllPlacesFromFirestore(null, null, context)
            withContext(Dispatchers.Main){
                placeListLiveData.value = list
            }
        }
    }

    private fun getPlacesFromFirestoreAccordingUserLocation(location: Location, radius: String){
        viewModelScope.launch(Dispatchers.IO) {
            val list = firestoreDataRepository.getAllPlacesFromFirestore(location, radius, context)
            withContext(Dispatchers.Main){
                placeListLiveData.value = list
            }
        }
    }

    fun getOnlyPlaceFromFirestore(){
        liveDataMerger.removeSource(placeTextSearchListLiveData)
    }

    fun init(){
        getAllPlacesFromFirestore()
        liveDataMerger.addSource(placeListLiveData, Observer {
            mergeList(it, placeTextSearchListLiveData.value)
        })
    }

    fun initViewModel(location: String, radius: String, query: String, locationForFirestore: Location){
        getPlacesFromFirestoreAccordingUserLocation(locationForFirestore, radius)
        getTextSearch(location, radius, query, context.getString(R.string.api_key_google))
        liveDataMerger.removeSource(placeListLiveData)
        liveDataMerger.addSource(placeListLiveData, Observer {
            mergeList(it, placeTextSearchListLiveData.value)
        })
        liveDataMerger.removeSource(placeTextSearchListLiveData)
        liveDataMerger.addSource(placeTextSearchListLiveData, Observer {
            mergeList(placeListLiveData.value, it)
        })
    }


    private fun mergeList(placeDetailedList: List<PlaceForList>?, placeTextSearchList: List<PlaceForList>?){
        if(placeDetailedList != null && placeTextSearchList != null) {
            val listPlaces = placeDetailedList + placeTextSearchList
            liveDataMerger.value = listPlaces.distinct()
        }else if(placeDetailedList != null && placeTextSearchList == null){
            liveDataMerger.value = placeDetailedList
        }else{
            return
        }



    }

    fun filter(query: String) {
        val listFiltered = ArrayList<PlaceForList>()
        for(place in allPlaceLiveData.value!!) {
            if(place.name.toLowerCase(Locale.ROOT).contains(query)||place.type.toLowerCase(Locale.ROOT).contains(query)){
                listFiltered.add(place)
            }
        }
        filteredPlacesLiveData.value = listFiltered
    }
}