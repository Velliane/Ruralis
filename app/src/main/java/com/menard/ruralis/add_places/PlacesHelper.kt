package com.menard.ruralis.add_places

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.menard.ruralis.utils.Constants
import kotlinx.coroutines.tasks.await

open class PlacesHelper {

    open fun getPlacesCollection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(Constants.COLLECTION_PLACES)
    }

    open fun createPlaces(id: String?, tags: String?, type: String, name: String, address: String, openings: String, website: String, phone_number: String, photos: List<String?>, latitude: String, longitude: String): Task<Void> {
        val newPlace = PlaceDetailed(id, tags, type, name, address, photos, openings, website, phone_number, latitude, longitude,
            true
        )
        return getPlacesCollection().document(id!!).set(newPlace)
    }

    open fun getAllPlaces(): Task<QuerySnapshot> {
        return getPlacesCollection().get()
    }

    open suspend fun getPlaceById(id: String): DocumentSnapshot {
        return getPlacesCollection().document(id).get().await()
    }

}