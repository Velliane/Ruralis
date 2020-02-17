package com.menard.ruralis.add_places

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.menard.ruralis.add_places.Place
import com.menard.ruralis.utils.Constants
import kotlinx.coroutines.tasks.await

open class PlacesHelper {

    open fun getPlacesCollection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(Constants.COLLECTION_PLACES)
    }

    open fun createPlaces(type: String, name: String, address: String, latitude: String, longitude: String): Task<Void> {
        val ref = getPlacesCollection().document()
        val id = ref.id
        val newPlace = Place(
            id,
            type,
            name,
            address,
            latitude,
            longitude,
            true
        )
        return ref.set(newPlace)
    }

    open suspend fun getAllPlaces(): List<DocumentSnapshot> {
        val snapshot = getPlacesCollection().get().await()
        return snapshot.documents
    }
}