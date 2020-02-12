package com.menard.ruralis.search_places.textsearch_model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OpeningHours {
    @SerializedName("open_now")
    @Expose
    var openNow: Boolean? = null

}