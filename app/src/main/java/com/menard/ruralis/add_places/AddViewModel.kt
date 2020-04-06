package com.menard.ruralis.add_places

import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.menard.ruralis.R
import com.menard.ruralis.add_places.geocode_model.GeocodeRepository
import com.menard.ruralis.data.FirestoreDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class AddViewModel(
    private val firestoreDataRepository: FirestoreDataRepository,
    private val geocodeRepository: GeocodeRepository,
    private val context: Context
) : ViewModel() {

    private val placeDetailedLiveData = MutableLiveData<PlaceDetailed>()

    fun savePlace(id: String?, type: String, name: String, address: String, openings: List<String>?, website: String, phone_number: String, edit: Boolean, countryCode: String, key: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val location = geocodeRepository.getLatLng(address, countryCode, key)
            withContext(Dispatchers.Main){
                firestoreDataRepository.savePlaceInFirestore(id, type, name, address, emptyList(), openings, website, phone_number, location?.latitude.toString(), location?.longitude.toString(), edit)
            }
        }
    }

    fun getTypeEnumList(): List<TypesEnum> {
        val list = ArrayList<TypesEnum>()
        for (type in TypesEnum.values()) {
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

    fun getPlaceDetailsById(id: String?): LiveData<PlaceDetailed> {
        viewModelScope.launch(Dispatchers.IO) {
            val placeDetailed = firestoreDataRepository.getPlaceDetailedById(id!!)
            withContext(Dispatchers.Main) {
                placeDetailedLiveData.value = placeDetailed
            }
        }
        return placeDetailedLiveData
    }
}