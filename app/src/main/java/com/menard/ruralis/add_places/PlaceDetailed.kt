package com.menard.ruralis.add_places

import java.io.Serializable


data class PlaceDetailed  (

    val placeId: String = "",

    val type: String = "",
    val name: String = "",
    val address: String = "",
    //val openingHours: Map<String, String>,
    val photos: List<String>? = null,

    val latitude: String? = "",
    val longitude: String? = "",

    val fromRuralis: Boolean = false

) : Serializable