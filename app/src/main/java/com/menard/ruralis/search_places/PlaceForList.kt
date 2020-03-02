package com.menard.ruralis.search_places

data class PlaceForList (

    val placeId: String = "",
    val name: String = "",
    val type: String = "",
    val photos: List<String>? = null,
    val latitude: String? = "",
    val longitude: String? = "",
    val fromRuralis: Boolean = false

)