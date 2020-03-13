package com.menard.ruralis.search_places.list

import androidx.lifecycle.*
import com.menard.ruralis.data.FirestoreDataRepository
import com.menard.ruralis.data.GoogleApiRepository
import com.menard.ruralis.search_places.PlaceForList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListViewModel(private val googleApiRepository: GoogleApiRepository, private val firestoreDataRepository: FirestoreDataRepository): ViewModel() {

    val placeListLiveData = MutableLiveData<List<PlaceForList>>()
    val placeTextSearchListLiveData = MutableLiveData<List<PlaceForList>>()

    val liveDataMerger = MediatorLiveData<List<PlaceForList>>()
    val allPlaceLiveData: LiveData<List<PlaceForList>> get() = liveDataMerger

//    init {
//        getAllPlacesFromFirestore()
//        liveDataMerger.addSource(placeListLiveData, Observer {
//            mergeList(it, placeTextSearchListLiveData.value)
//        })
//        liveDataMerger.addSource(placeTextSearchListLiveData, Observer {
//            mergeList(placeListLiveData.value, it)
//        })
//    }

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
        getAllPlacesFromFirestore()
        getTextSearch(location, radius, query, key)
        liveDataMerger.addSource(placeListLiveData){
                mergeList(it, placeTextSearchListLiveData.value)
        }
        liveDataMerger.addSource(placeTextSearchListLiveData){
                 mergeList(placeListLiveData.value, it)
        }
    }

    private fun mergeList(placeFromFirestoreList: List<PlaceForList>?, placeTextSearchList: List<PlaceForList>?){
        val listPlaces  = placeFromFirestoreList?.let { list1 ->
            placeTextSearchList?.let { list2 -> list1 + list2 } }
        liveDataMerger.value = listPlaces
    }
}