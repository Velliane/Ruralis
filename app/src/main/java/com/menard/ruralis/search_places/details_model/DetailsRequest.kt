package com.menard.ruralis.search_places.details_model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DetailsRequest {
    @SerializedName("html_attributions")
    @Expose
    var htmlAttributions: List<Any>? = null
    @SerializedName("result")
    @Expose
    var result: ResultDetails? = null
    @SerializedName("status")
    @Expose
    var status: String? = null

}