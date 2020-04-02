package com.menard.ruralis.data

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.menard.ruralis.add_places.PlaceDetailed
import com.menard.ruralis.add_places.PlacesHelper
import com.menard.ruralis.details.comments.Comments
import com.menard.ruralis.details.comments.CommentsHelper
import com.menard.ruralis.search_places.PlaceForList
import kotlinx.coroutines.tasks.await
import java.util.*
import kotlin.collections.ArrayList


class FirestoreDataRepository {

    private val placesHelper = PlacesHelper()
    private val commentsHelper = CommentsHelper()

    suspend fun getAllPlacesFromFirestore(): List<PlaceForList> {
        val list = ArrayList<PlaceForList>()
        val listDocument = placesHelper.getAllPlaces()
        for (document in listDocument) {
            val photos = document.data!!["list_photos"]
            var listPhoto: List<String>? = null
            if (photos != null) {
                listPhoto = photos as List<String>
            }
            val placeForList = PlaceForList(
                document.id,
                document.getString("name")!!,
                document.getString("type")!!,
                listPhoto,
                document.getString("latitude"),
                document.getString("longitude"),
                document.getBoolean("fromRuralis")!!
            )
            list.add(placeForList)
        }
        return list
    }

    suspend fun getPlaceDetailedById(place_id: String): PlaceDetailed? {
        val document = placesHelper.getPlaceById(place_id)
        return document.toObject(PlaceDetailed::class.java)
    }


    suspend fun getPlaceForListById(place_id: String): PlaceForList {
        val document = placesHelper.getPlaceById(place_id)
        val photos = document.data!!["list_photos"]
        var listPhoto: List<String>? = null
        if (photos != null) {
            listPhoto = photos as List<String>
        }
        return PlaceForList(
            document.id,
            document.getString("name")!!,
            document.getString("type")!!,
            listPhoto,
            document.getString("latitude"),
            document.getString("longitude"),
            document.getBoolean("fromRuralis")!!
        )
    }

    suspend fun getPlaceFromFirestoreById(id: String?): PlaceDetailed {
        return placesHelper.getPlaceById(id!!).toObject<PlaceDetailed>(
            PlaceDetailed::class.java
        )!!
    }

    suspend fun savePlaceInFirestore(
        id: String?,
        type: String,
        name: String,
        address: String,
        photos: List<Uri>,
        openings: List<String>?,
        website: String,
        phoneNumber: String,
        latitude: String,
        longitude: String,
        edit: Boolean
    ) {
        if (!edit) {
            //-- New object --//
            val ref = placesHelper.getPlacesCollection().document()
            val newId = ref.id
            val listPhotos = getListOfPath(photos, newId)
            placesHelper.createPlaces(newId, type, name, address, openings, website, phoneNumber, listPhotos, latitude, longitude)
        } else {
            //-- Update object --//
            val listPhotos = getListOfPath(photos, id)
            placesHelper.createPlaces(id, type, name, address, openings, website, phoneNumber, listPhotos, latitude, longitude)
        }
    }

    suspend fun getCommentsOfPlace(id: String?): List<Comments> {
        val listComments = ArrayList<Comments>()
        val querySnapshot = commentsHelper.getAllComments(id)
        for (query in querySnapshot!!.documents) {
            val comment = query.toObject(Comments::class.java)!!
            listComments.add(comment)
        }
        return listComments
    }

    //-- Image --//

    private suspend fun saveImageToFirestore(uri: Uri, id: String?): String {
        var stringPath = ""
        val name = UUID.randomUUID().toString()
        val storageReference = FirebaseStorage.getInstance().getReference("/images/$id/$name")
        storageReference.putFile(uri).addOnSuccessListener {
            Log.d("PHOTO", "Photo successfully saved in Firebase")
            stringPath = it.metadata!!.path
        }.await()
        return stringPath
    }

    suspend fun getListOfPath(listUri: List<Uri>, id: String?): List<String> {
        val listPhotos = ArrayList<String>()
        for (uri in listUri) {
            listPhotos.add(saveImageToFirestore(uri, id))
        }
        return listPhotos
    }
}