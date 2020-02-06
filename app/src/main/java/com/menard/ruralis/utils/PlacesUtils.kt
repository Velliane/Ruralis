package com.menard.ruralis.utils

import android.location.Location
import java.util.*

fun distanceToUser(restaurantLocation: Location, userLocation: Location): String {
    val realDistance = userLocation.distanceTo(restaurantLocation)

    return if (realDistance >= 1000) {
        String.format(Locale.getDefault(), "%.2f", realDistance / 1000) + "km"
    } else {
        (realDistance.toInt().toString()) + "m"
    }
}