package com.menard.ruralis.search_places


data class Place  (

    val placeId: String = "",

    val type: String = "",
    val name: String = "",
    val address: String = "",
    //val tags: List<String> = ArrayList(),
    //val openingHours: Map<String, String>,

    val latitude: String = "",
    val longitude: String = "",

    val from: Boolean = false

)