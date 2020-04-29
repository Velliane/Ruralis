package com.menard.ruralis.details.photos

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.menard.ruralis.utils.Constants
import kotlinx.coroutines.tasks.await

open class PhotoHelper {

    private fun getPhotosCollection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(Constants.COLLECTION_PHOTOS)
    }

    fun createPhoto(id: String, name: String, uriPhoto: String): Task<Void> {
        val photo = Photo(uriPhoto, name, false, id)
        return getPhotosCollection().document(name).set(photo)
    }

    fun getAllPhotosById(id: String): Task<QuerySnapshot> {
        return getPhotosCollection().whereEqualTo("place_id", id).get()
    }

    fun getPhotosInRealTime(id: String): Query {
        return getPhotosCollection().whereEqualTo("place_id", id)
    }
}