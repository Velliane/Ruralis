package com.menard.ruralis.add_places

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.menard.ruralis.data.FirestoreDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddViewModel(private val firestoreDataRepository: FirestoreDataRepository) : ViewModel() {

    private val photosLiveData = MutableLiveData<List<String>>()
    private val placeDetailedLiveData = MutableLiveData<PlaceDetailed>()

    fun savePlace(
        id: String?,
        type: String,
        name: String,
        address: String,
        openings: List<String>?,
        website: String,
        phone_number: String,
        photos: List<Uri>,
        latitude: String,
        longitude: String,
        edit: Boolean
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            firestoreDataRepository.savePlaceInFirestore(
                id,
                type,
                name,
                address,
                photos,
                openings,
                website,
                phone_number,
                latitude,
                longitude,
                edit
            )
        }
    }

    fun getTypeEnumList(): List<Types> {
        val list = ArrayList<Types>()
        for (type in Types.values()) {
            list.add(type)
        }
        return list
    }


    fun addOpeningToRecyclerView(day: String?, hours: String?): LiveData<String?> {
        val openingLiveData = MutableLiveData<String>()
        return if (day == "" || hours == "") {
            openingLiveData.value = null
            openingLiveData
        } else {
            val opening = "$day: $hours"
            openingLiveData.value = opening
            openingLiveData
        }
    }

    fun getListOfPhotos(uriList: List<Uri>, id: String?): List<String>? {
        viewModelScope.launch(Dispatchers.IO) {
            val list = firestoreDataRepository.getListOfPath(uriList, id)
            withContext(Dispatchers.Main) {
                photosLiveData.value = list
            }
        }
        return photosLiveData.value
    }

    fun getPlaceDetailsById(id: String): LiveData<PlaceDetailed> {
        viewModelScope.launch(Dispatchers.IO) {
            val placeDetailed = firestoreDataRepository.getPlaceDetailedById(id)
            withContext(Dispatchers.Main) {
                placeDetailedLiveData.value = placeDetailed
            }
        }
        return placeDetailedLiveData
    }
}