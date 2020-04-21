package com.menard.ruralis.utils

import android.content.Context
import android.content.DialogInterface
import android.location.Location
import android.net.ConnectivityManager
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnFailureListener
import com.menard.ruralis.R
import com.menard.ruralis.add_places.DayEnum
import com.menard.ruralis.search_places.PlaceForList
import com.menard.ruralis.search_places.map.MarkerTag
import java.lang.StringBuilder
import java.util.*

fun distanceToUser(placeLocation: Location, userLocation: Location): String {
    val realDistance = userLocation.distanceTo(placeLocation)

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

fun isInternetAvailable(context: Context): Boolean {
    val isConnected: Boolean
    val connectivityManager =
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
    val networkInfo = connectivityManager.activeNetworkInfo
    isConnected = networkInfo != null && networkInfo.isConnected
    return isConnected
}

fun onFailureListener(context: Context): OnFailureListener {
    return OnFailureListener { e: Exception ->
        Log.d("Error", e.localizedMessage!!)
        val builder =
            AlertDialog.Builder(context, R.style.AppTheme)
        builder.setMessage(context.getString(R.string.error_firestore))
            .setNegativeButton(
                "Ok",
                DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int -> }
            )
            .create().show()
    }
}

fun changeOpeningHoursToLocaleLanguage(opening: String, context: Context): String{
    val sb = StringBuilder("")
    val day = opening.substringBefore(":")
    val hours = opening.substringAfter(":")
    Log.d("HOURS", hours)
    for(item in DayEnum.values()){
        if (day.toUpperCase(Locale.ROOT) == item.toString()) {
            Log.d("ENUM", item.toString())
            sb.append(context.getString(item.res))
            Log.d("SB", sb.toString())
        }
    }
    if(hours.contains("Closed")){
        sb.append(" : ${context.getString(R.string.closed)}")
    }else{
        sb.append(" :$hours")
    }

    Log.d("SB", sb.toString())
    return sb.toString()
}

fun setTypeForPlacesFromGoogleMaps(listTypes: List<String>, context: Context): String {
    return when {
        listTypes.contains("grocery_or_supermarket") -> {
            context.getString(R.string.type_product_shop)
        }
        listTypes.contains("liquor_store") -> {
            context.getString(R.string.type_alcool)
        }
        else -> {
            context.getString(R.string.type_food_general)
        }
    }

}

fun transformListOfOpeningToString(openings: List<String>?):String{
    val sb = StringBuilder("")
    openings?.forEachIndexed{ index, item ->
        sb.append(item)
        if(index < openings.size-1){
            sb.append(",")
        }
    }
    return sb.toString()
}
