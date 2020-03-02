package com.menard.ruralis.data

import com.menard.ruralis.add_places.PlaceDetailed
import com.menard.ruralis.add_places.PlacesHelper
import com.menard.ruralis.search_places.PlaceForList


class FirestoreDataRepository {

    suspend fun getAllPlacesFromFirestore(): List<PlaceForList> {
        val list = ArrayList<PlaceForList>()
        val placesHelper = PlacesHelper()
        val listDocument = placesHelper.getAllPlaces()
        for(document in listDocument){
            val listPhoto: List<String> = listOf(document.get("photos").toString())
           val placeForList = PlaceForList(document.id, document.getString("name")!!, document.getString("type")!!, listPhoto, document.getString("latitude"), document.getString("longitude"), document.getBoolean("fromRuralis")!!)
//            val place = document.toObject<PlaceForList>(
//                PlaceForList::class.java)
            list.add(placeForList)
        }
        return list
    }

    suspend fun getPlaceFromFirestoreById(id: String): PlaceDetailed {
        val placesHelper = PlacesHelper()
        return placesHelper.getPlaceById(id).toObject<PlaceDetailed>(
            PlaceDetailed::class.java)!!
    }
}