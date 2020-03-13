package com.menard.ruralis.data

import com.google.android.libraries.places.api.model.Place
import com.menard.ruralis.add_places.PlaceDetailed
import com.menard.ruralis.details.comments.Comments
import com.menard.ruralis.search_places.PlaceForList
import com.menard.ruralis.utils.GooglePlacesAPI

class GoogleApiRepository {

    private val retrofit = GooglePlacesAPI.retrofit.create(GooglePlacesAPI::class.java)

    suspend fun getTextSearch(location: String, radius: String, query: String, key: String): ArrayList<PlaceForList> {
        val results = retrofit.getTextSearch(location, radius, query, key).results
        val list = ArrayList<PlaceForList>()
        for (result in results!!) {
            val lat = result.geometry!!.location!!.lat.toString()
            val lng = result.geometry!!.location!!.lng.toString()
            //-- Get first photo's reference --//
            if (result.photos != null) {
                val photos = result.photos!!
                val listRef = ArrayList<String>()
                if (photos.isNotEmpty()) {
                    listRef.add(photos[0].photoReference!!)
                }
                val place = PlaceForList(result.placeId!!, result.name!!, result.types!![0], listRef, lat, lng, false)
                list.add(place)
            }
        }
        return list
    }


    suspend fun getDetails(place_id: String, fields: String, key: String): PlaceDetailed {

        val result = retrofit.getDetailsById(place_id, fields, key).result!!
        val lat = result.geometry!!.location!!.lat.toString()
        val lng = result.geometry!!.location!!.lng.toString()
        val photos = result.photos!!
        val listRef = ArrayList<String>()
        for (photo in photos) {
            listRef.add(photo.photoReference!!)
        }
        return PlaceDetailed(result.placeId!!, "Etablissement trouvé sur GoogleMap", result.name!!, result.vicinity.toString(), listRef, result.website.toString(), result.formattedPhoneNumber.toString(), lat, lng, false
        )
    }

    suspend fun getPlaceFavorites(place_id: String, fields: String, key: String): PlaceForList {
        val result = retrofit.getDetailsById(place_id, fields, key).result!!
        val lat = result.geometry!!.location!!.lat.toString()
        val lng = result.geometry!!.location!!.lng.toString()
        val photos = result.photos!!
        val listRef = ArrayList<String>()
        for (photo in photos) {
            listRef.add(photo.photoReference!!)
        }
        return PlaceForList(result.placeId!!, result.name!!, result.types!![0], listRef, lat, lng, false)
    }

    suspend fun getComments(place_id: String, fields: String, key: String): List<Comments> {
        val result = retrofit.getDetailsById(place_id, fields, key).result!!
        val listOfComments = ArrayList<Comments>()
        val listOfReview = result.reviews
        if(listOfReview != null) {
            for (review in listOfReview) {
                val comments = Comments(
                    review.authorName.toString(),
                    review.text.toString()
                )
                listOfComments.add(comments)
            }
        }else{
            listOfComments.isEmpty()
        }
        return listOfComments
    }
}

