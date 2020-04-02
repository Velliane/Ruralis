package com.menard.ruralis.add_places

import java.io.Serializable


data class PlaceDetailed  (

    val placeId: String? = "",

    val type: String = "",
    val name: String = "",
    val address: String = "",
    val photos: List<String>? = null,
    val openingsHours: List<String>? = null,
    val website: String = "",
    val phone_number: String = "",

    val latitude: String? = "",
    val longitude: String? = "",

    val fromRuralis: Boolean = false

) : Serializable