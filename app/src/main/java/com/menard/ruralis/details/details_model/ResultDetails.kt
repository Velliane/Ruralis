package com.menard.ruralis.details.details_model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResultDetails {
    @SerializedName("formatted_phone_number")
    @Expose
    var formattedPhoneNumber: String? = null
    @SerializedName("geometry")
    @Expose
    var geometry: Geometry? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("opening_hours")
    @Expose
    var openingHours: OpeningHours? = null
    @SerializedName("photos")
    @Expose
    var photos: List<Photo>? =
        null
    @SerializedName("place_id")
    @Expose
    var placeId: String? = null
    @SerializedName("rating")
    @Expose
    var rating: Double? = null
    @SerializedName("reviews")
    @Expose
    var reviews: List<Review>? = null
    @SerializedName("types")
    @Expose
    var types: List<String>? = null
    @SerializedName("vicinity")
    @Expose
    var vicinity: String? = null
    @SerializedName("website")
    @Expose
    var website: String? = null

}