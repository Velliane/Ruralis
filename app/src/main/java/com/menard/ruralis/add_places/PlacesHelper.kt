package com.menard.ruralis.add_places

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.menard.ruralis.utils.Constants
import kotlinx.coroutines.tasks.await

open class PlacesHelper {

    open fun getPlacesCollection(): CollectionReference {
        val instance = FirebaseFirestore.getInstance()
        //val settings = FirebaseFirestoreSettings.Builder().setTimestampsInSnapshotsEnabled(true).build()
        //instance.firestoreSettings = settings
        return instance.collection(Constants.COLLECTION_PLACES)
    }


    open fun createPlaces(type: String, name: String, address: String, photos: ArrayList<String>?, latitude: String, longitude: String): Task<Void> {
        val ref = getPlacesCollection().document()
        val id = ref.id
        val newPlace = PlaceDetailed(
            id,
            type,
            name,
            address,
            photos,
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

    open suspend fun getPlaceById(id: String): DocumentSnapshot {
        return getPlacesCollection().document(id).get().await()
    }
}