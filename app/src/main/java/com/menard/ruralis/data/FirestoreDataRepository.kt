package com.menard.ruralis.data

import android.content.Context
import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.menard.ruralis.details.photos.Photo
import com.menard.ruralis.add_places.PlaceDetailed
import com.menard.ruralis.add_places.PlacesHelper
import com.menard.ruralis.add_places.TypesEnum
import com.menard.ruralis.details.comments.Comments
import com.menard.ruralis.details.comments.CommentsHelper
import com.menard.ruralis.details.photos.PhotoHelper
import com.menard.ruralis.quiz.Question
import com.menard.ruralis.quiz.QuizHelper
import com.menard.ruralis.search_places.PlaceForList
import com.menard.ruralis.utils.onFailureListener
import kotlin.collections.ArrayList


class FirestoreDataRepository {

    private val placesHelper = PlacesHelper()
    private val commentsHelper = CommentsHelper()
    private val photoHelper = PhotoHelper()
    private val quizHelper = QuizHelper()

    suspend fun getAllPlacesFromFirestore(
        userLocation: Location?,
        radius: String?,
        context: Context
    ): List<PlaceForList> {
        val list = ArrayList<PlaceForList>()
        placesHelper.getAllPlaces().addOnSuccessListener {
            val listDocument = it.documents
            for (document in listDocument) {
                if(userLocation != null && radius != null){
                    val location = Location("")
                    location.latitude = document.getString("latitude")!!.toDouble()
                    location.longitude = document.getString("longitude")!!.toDouble()
                    if (userLocation.distanceTo(location) < radius.toInt()) {
                        val listOfPhoto = getListOfPhotosFromFirestore(document.id, context)
                        var uriPhoto: String? = null
                        if (listOfPhoto.isNotEmpty()) {
                            uriPhoto = listOfPhoto[0].uri
                        }
                        val type = document.getString("type")
                        val placeForList = PlaceForList(
                            document.id,
                            document.getString("name")!!,
                            context.getString(TypesEnum.valueOf(type!!).res),
                            uriPhoto,
                            document.getString("latitude"),
                            document.getString("longitude"),
                            document.getBoolean("fromRuralis")!!
                        )
                        list.add(placeForList)
                    }
                }else{
                    val listOfPhoto = getListOfPhotosFromFirestore(document.id, context)
                    var uriPhoto: String? = null
                    if (listOfPhoto.isNotEmpty()) {
                        uriPhoto = listOfPhoto[0].uri
                    }
                    val type = document.getString("type")
                    val placeForList = PlaceForList(
                        document.id,
                        document.getString("name")!!,
                        context.getString(TypesEnum.valueOf(type!!).res),
                        uriPhoto,
                        document.getString("latitude"),
                        document.getString("longitude"),
                        document.getBoolean("fromRuralis")!!
                    )
                    list.add(placeForList)
                }
            }

            }.addOnFailureListener (onFailureListener(context))

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
            listPhoto?.get(0),
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

    fun savePlaceInFirestore(
        id: String?,
        type: String,
        name: String,
        address: String,
        photos: List<String?>,
        openings: String,
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
            placesHelper.createPlaces(
                newId,
                type,
                name,
                address,
                openings,
                website,
                phoneNumber,
                photos,
                latitude,
                longitude
            )
        } else {
            //-- Update object --//
            placesHelper.createPlaces(
                id, type, name, address, openings, website, phoneNumber, photos, latitude, longitude
            )
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

    fun addComment(id: String?, name: String, comment: String) {
        commentsHelper.addComment(id!!, name, comment)
    }

    //-- Image --//

    fun getListOfPhotosFromFirestore(id: String, context: Context): ArrayList<Photo> {
        val listOfPhotos = ArrayList<Photo>()
        photoHelper.getAllPhotosById(id).addOnSuccessListener {
            it?.documents?.forEach { document ->
                val photo = Photo(
                    document.getString("uri"),
                    document.id,
                    document.getBoolean("selected"),
                    document.getString("place_id")
                )
                listOfPhotos.add(photo)
            }
        }.addOnFailureListener (onFailureListener(context))
        return listOfPhotos
    }

    fun savePhoto(id: String?, name: String, uriPhoto: String) {
        photoHelper.createPhoto(id!!, name, uriPhoto)
    }

    //-- Quiz data repository --//
    suspend fun getListOfQuestion(listId: List<Int>): List<Question?> {
        val list = ArrayList<Question?>()
        for(id in listId){
            val listDocuments = quizHelper.getQuestionsForQuiz(id)?.documents
            if(listDocuments != null && listDocuments.isNotEmpty()){
                val question = listDocuments[0].toObject(Question::class.java)
                list.add(question)
            }
        }
        return list
    }

}