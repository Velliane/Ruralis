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

    private val liveDataMerger = MediatorLiveData<List<PlaceForList>>()
    val allPlaceLiveData: LiveData<List<PlaceForList>> = liveDataMerger



    fun initViewModel(
        location: String,
        radius: String,
        query: String,
        locationForFirestore: Location,
        fromMaps: Boolean){

        getPlacesFromFirestoreAccordingUserLocation(locationForFirestore, radius)
        getTextSearch(location, radius, query, context.getString(R.string.api_key_google))
        if (fromMaps){
            if(!liveDataMerger.hasObservers()){
                liveDataMerger.addSource(placeListLiveData, Observer {
                    mergeList(it, placeTextSearchListLiveData.value)
                })
                liveDataMerger.addSource(placeTextSearchListLiveData, Observer {
                    mergeList(placeListLiveData.value, it)
                })
            }
        }else{
            if (!liveDataMerger.hasObservers()){
                liveDataMerger.addSource(placeListLiveData, Observer {
                    mergeList(it, null)
                })
            }else{
                liveDataMerger.removeSource(placeTextSearchListLiveData)
            }
        }
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


    /**
     * Get list of places from TextSearch API
     */
    fun getTextSearch(location: String, radius: String, query: String, key: String){
        viewModelScope.launch(Dispatchers.IO) {
            val list = googleApiRepository.getTextSearch(context, location, radius, query, key)
            withContext(Dispatchers.Main){
                placeTextSearchListLiveData.value = list
            }
        }
    }

    /**
     * Get list of places from Firestore
     * @param location user's location
     * @param radius search radius selected by user
     */
    fun getPlacesFromFirestoreAccordingUserLocation(location: Location, radius: String){
        viewModelScope.launch(Dispatchers.IO) {
            val list = firestoreDataRepository.getAllPlacesFromFirestore(location, radius, context)
            withContext(Dispatchers.Main){
                placeListLiveData.value = list
            }
        }
    }

    /**
     * Filter list of places according query search
     * @param query
     */
    fun filter(query: String) {
        val listFiltered = ArrayList<PlaceForList>()
        if(allPlaceLiveData.value != null){
            for(place in allPlaceLiveData.value!!) {
                if(place.name.toLowerCase(Locale.ROOT).contains(query)||place.type.toLowerCase(Locale.ROOT).contains(query)){
                    listFiltered.add(place)
                }
            }
            filteredPlacesLiveData.value = listFiltered
        }
    }
}