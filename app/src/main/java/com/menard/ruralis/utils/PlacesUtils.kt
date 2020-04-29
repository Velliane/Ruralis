package com.menard.ruralis.utils

import android.content.Context
import android.content.DialogInterface
import android.location.Location
import android.net.ConnectivityManager
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.room.util.StringUtil
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnFailureListener
import com.menard.ruralis.R
import com.menard.ruralis.add_places.DayEnum
import com.menard.ruralis.add_places.OpeningEnum
import com.menard.ruralis.search_places.PlaceForList
import com.menard.ruralis.search_places.map.MarkerTag
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList

fun setMarker(place: PlaceForList, googleMap: GoogleMap, latLng: LatLng, type: String) {
    if (!place.fromRuralis) {
        val markerOptions = MarkerOptions().position(latLng)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            .title(place.name)
            .snippet(type)
        val marker = googleMap.addMarker(markerOptions)
        val markerTag = MarkerTag(place.placeId, place.fromRuralis, place.photos)
        marker.tag = markerTag
    } else {
        val markerOptions = MarkerOptions().position(latLng)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            .title(place.name)
            .snippet(type)
        val marker = googleMap.addMarker(markerOptions)
        val markerTag = MarkerTag(place.placeId, place.fromRuralis, place.photos)
        marker.tag = markerTag
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
        sb.append(" : $hours")
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

fun parseLocalDateTimeToString(date: LocalDateTime): String {
    val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return date.format(dateTimeFormatter)
}

/**
 * Add progress drawable
 */
fun getProgressDrawableSpinner(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 5f
        centerRadius = 40f
        start()
    }
}

/**
 * Load restaurant's photo
 */
fun ImageView.loadPlacePhoto(@Nullable url: String?, @Nullable int:Int?, progressDrawable: CircularProgressDrawable){
    val options = RequestOptions().placeholder(progressDrawable).error(R.drawable.no_image_available_64)

    if(url != null) {
        Glide.with(this.context).setDefaultRequestOptions(options.centerCrop()).load(Uri.parse(url)).into(this)
    }else{
        Glide.with(this.context).setDefaultRequestOptions(options).load(int).into(this)
    }
}

fun checkIfEstablishmentIsOpenNow(listOfOpenings: List<String>?, context: Context): Boolean {

    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneOffset.UTC)
    var isOpenNow= false
    if(listOfOpenings == null || listOfOpenings.isEmpty()){
        isOpenNow = false
    }else{
        val now = LocalDateTime.now()
        val dayOfToday = now.dayOfWeek.toString()
        listOfOpenings.forEach {
            if (it.toLowerCase(Locale.ROOT).contains(context.getString(DayEnum.valueOf(dayOfToday.toUpperCase(Locale.ROOT)).res).toLowerCase(
                    Locale.ROOT))){
                val opening = it.substringBefore("/").substringAfter(": ")
                val closing = it.substringAfter("/")
                val openingTime = LocalTime.parse(opening, timeFormatter)
                val closingTime = LocalTime.parse(closing, timeFormatter)
                isOpenNow = openingTime.isBefore(now.toLocalTime()) && closingTime.isAfter(now.toLocalTime())
            }
        }
    }
    return isOpenNow
}

fun getListOfDay(context: Context): List<String>{
    val list = ArrayList<String>()
    for (day in OpeningEnum.values()){
        list.add(context.getString(day.res))
    }
    return list
}