package com.menard.ruralis.add_places

import java.io.Serializable


data class Place  (

    val placeId: String = "",

    val type: String = "",
    val name: String = "",
    val address: String = "",
    //val tags: List<String> = ArrayList(),
    //val openingHours: Map<String, String>,

    val latitude: String? = "",
    val longitude: String? = "",

    val fromRuralis: Boolean = false

) : Serializable