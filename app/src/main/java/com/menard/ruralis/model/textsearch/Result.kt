package com.menard.ruralis.model.textsearch

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Result {
    @SerializedName("formatted_address")
    @Expose
    var formattedAddress: String? = null
    @SerializedName("geometry")
    @Expose
    var geometry: Geometry? = null
    @SerializedName("icon")
    @Expose
    var icon: String? = null
    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("opening_hours")
    @Expose
    var openingHours: OpeningHours? = null
    @SerializedName("photos")
    @Expose
    var photos: List<Photo>? = null
    @SerializedName("place_id")
    @Expose
    var placeId: String? = null
    @SerializedName("plus_code")
    @Expose
    var plusCode: PlusCode? = null
    @SerializedName("rating")
    @Expose
    var rating: Float? = null
    @SerializedName("reference")
    @Expose
    var reference: String? = null
    @SerializedName("types")
    @Expose
    var types: List<String>? = null
    @SerializedName("user_ratings_total")
    @Expose
    var userRatingsTotal: Float? = null

}