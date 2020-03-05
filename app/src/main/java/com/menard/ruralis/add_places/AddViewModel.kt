package com.menard.ruralis.add_places

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.menard.ruralis.data.FirestoreDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddViewModel(private val firestoreDataRepository: FirestoreDataRepository): ViewModel() {

    fun savePlace(id: String, type: String, name: String, address: String, website:String, phone_number:String, photos: List<Uri>, latitude: String, longitude: String, edit: Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            firestoreDataRepository.savePlaceInFirestore(id, type, name, address, photos, website, phone_number, latitude, longitude, edit)
        }
    }

    fun getTypeEnumList(): List<Types> {
        val list = ArrayList<Types>()
        for(type in Types.values()) {
            list.add(type)
        }
        return list
    }
}