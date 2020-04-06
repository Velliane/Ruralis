package com.menard.ruralis.utils

import android.location.Location
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.menard.ruralis.search_places.PlaceForList
import com.menard.ruralis.search_places.map.MarkerTag
import java.util.*

fun distanceToUser(restaurantLocation: Location, userLocation: Location): String {
    val realDistance = userLocation.distanceTo(restaurantLocation)

    return if (realDistance >= 1000) {
        String.format(Locale.getDefault(), "%.2f", realDistance / 1000) + "km"
    } else {
        (realDistance.toInt().toString()) + "m"
    }
}

fun setMarker(place: PlaceForList, googleMap: GoogleMap, latLng: LatLng) {
    if (!place.fromRuralis) {
        val markerOptions = MarkerOptions().position(latLng)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            .title(place.name)
        val marker = googleMap.addMarker(markerOptions)
        val markerTag = MarkerTag(place.placeId, place.fromRuralis, place.photos)
        marker.tag = markerTag
    } else {
        val markerOptions = MarkerOptions().position(latLng)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            .title(place.name)
        val marker = googleMap.addMarker(markerOptions)
        val markerTag = MarkerTag(place.placeId, place.fromRuralis, place.photos)
        marker.tag = markerTag
    }
}