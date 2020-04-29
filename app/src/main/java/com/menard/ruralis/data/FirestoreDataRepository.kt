package com.menard.ruralis.data

import android.content.Context
import android.location.Location
import android.util.Log
import com.jakewharton.threetenabp.AndroidThreeTen
import com.menard.ruralis.add_places.PlaceDetailed
import com.menard.ruralis.add_places.PlacesHelper
import com.menard.ruralis.add_places.TypesEnum
import com.menard.ruralis.details.comments.Comments
import com.menard.ruralis.details.comments.CommentsHelper
import com.menard.ruralis.details.photos.Photo
import com.menard.ruralis.details.photos.PhotoHelper
import com.menard.ruralis.quiz.Question
import com.menard.ruralis.quiz.QuizHelper
import com.menard.ruralis.search_places.PlaceForList
import com.menard.ruralis.utils.checkIfEstablishmentIsOpenNow
import kotlinx.coroutines.suspendAtomicCancellableCoroutine
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await


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

        AndroidThreeTen.init(context)
        val list = ArrayList<PlaceForList>()
        val places = placesHelper.getAllPlaces().await()
        val listDocument = places.documents
        for (document in listDocument) {
            if (userLocation != null && radius != null) {
                val location = Location("")
                location.latitude = document.getString("latitude")!!.toDouble()
                location.longitude = document.getString("longitude")!!.toDouble()
                if (userLocation.distanceTo(location) < radius.toInt()) {
                    val listOfPhotos = ArrayList<Photo>()
                    Log.i("PHOTOS", "Getting photos")
                    val query = photoHelper.getAllPhotosById(document.id).await()
                    Log.i("PHOTOS", "Got photos")
                    query.documents.forEach { photoDocument ->
                        val photo = Photo(
                            photoDocument.getString("uri"),
                            photoDocument.id,
                            photoDocument.getBoolean("selected"),
                            photoDocument.getString("place_id")
                        )
                        listOfPhotos.add(photo)
                    }
                    var uriPhoto: String? = null
                    if (listOfPhotos.isNotEmpty()) {
                        uriPhoto = listOfPhotos[0].uri
                    }
                    val openings = document.getString("openingsHours")
                    val listOfOpenings = ArrayList<String>()
                    if(openings != null && openings != ""){
                        openings.split(",").toTypedArray().all {
                            listOfOpenings.add(it)
                        }

                    }

                    val type = document.getString("type")
                    val placeForList = PlaceForList(
                        document.id,
                        document.getString("name")!!,
                        context.getString(TypesEnum.valueOf(type!!).res),
                        uriPhoto,
                        document.getString("latitude"),
                        document.getString("longitude"),
                        document.getString("address")!!,
                        document.getBoolean("fromRuralis")!!,
                        checkIfEstablishmentIsOpenNow(listOfOpenings, context),
                        document.getString("tags")!!
                    )
                    list.add(placeForList)
                }
            }
        }

        return list
    }

    suspend fun getPlaceDetailedById(place_id: String): PlaceDetailed? {
        val document = placesHelper.getPlaceById(place_id)
        return document.toObject(PlaceDetailed::class.java)
    }

    suspend fun getPlaceFromFirestoreById(id: String?): PlaceDetailed {
        return placesHelper.getPlaceById(id!!).toObject<PlaceDetailed>(
            PlaceDetailed::class.java
        )!!
    }

    fun savePlaceInFirestore(
        id: String?,
        tags: String?,
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
                tags,
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
                id,
                tags,
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
    fun savePhoto(id: String?, name: String, uriPhoto: String) {
        photoHelper.createPhoto(id!!, name, uriPhoto)
    }

    //-- Quiz data repository --//
    suspend fun getListOfQuestion(listId: List<Int>): List<Question?> {
        val list = ArrayList<Question?>()
        for (id in listId) {
            val listDocuments = quizHelper.getQuestionsForQuiz(id)?.documents
            if (listDocuments != null && listDocuments.isNotEmpty()) {
                val question = listDocuments[0].toObject(Question::class.java)
                list.add(question)
            }
        }
        return list
    }

    suspend fun getPhotosInRealTime(id: String)= suspendCancellableCoroutine<List<Photo>> {cont ->
        val snapshot = photoHelper.getPhotosInRealTime(id)
        val listOfPhotos = ArrayList<Photo>()
        snapshot.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            querySnapshot?.documents?.forEach { photoDocument ->
                val photo = Photo(
                    photoDocument.getString("uri"),
                    photoDocument.id,
                    photoDocument.getBoolean("selected"),
                    photoDocument.getString("place_id")
                )
                listOfPhotos.add(photo)
            }
        }
        cont.resumeWith(Result.success(listOfPhotos))
    }

}