package com.menard.ruralis.details.details_model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Open {
    @SerializedName("day")
    @Expose
    var day: Int? = null
    @SerializedName("time")
    @Expose
    var time: String? = null

}