package com.menard.ruralis.add_places

import java.io.Serializable


data class PlaceDetailed  (

    val placeId: String? = "",

    val tags: String? = null,
    val type: String = "",
    val name: String = "",
    val address: String = "",
    val photos: List<String?>? = emptyList(),
    val openingsHours: String? = null,
    val website: String = "",
    val phone_number: String = "",

    val latitude: String? = "",
    val longitude: String? = "",

    val fromRuralis: Boolean = false

) : Serializable