package com.menard.ruralis.add_places.geocode_model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.menard.ruralis.add_places.geocode_model.Northeast
import com.menard.ruralis.add_places.geocode_model.Southwest

class Viewport {
    @SerializedName("northeast")
    @Expose
    var northeast: Northeast? = null
    @SerializedName("southwest")
    @Expose
    var southwest: Southwest? = null

}