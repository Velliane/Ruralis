package com.menard.ruralis.details.photos

data class Photo (
    val uri: String? = "",
    val name: String? = "",
    val selected: Boolean? = false,
    val place_id: String? = ""
)