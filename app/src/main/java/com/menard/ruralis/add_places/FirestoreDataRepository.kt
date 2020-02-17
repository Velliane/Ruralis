package com.menard.ruralis.add_places


class FirestoreDataRepository {

    suspend fun getAllPlacesFromFirestore(): ArrayList<Place> {
        val list = ArrayList<Place>()
        val placesHelper = PlacesHelper()
        val listDocument = placesHelper.getAllPlaces()
        for(document in listDocument){
            val place = document.toObject<Place>(Place::class.java)
            list.add(place!!)
        }
        return list
    }
}