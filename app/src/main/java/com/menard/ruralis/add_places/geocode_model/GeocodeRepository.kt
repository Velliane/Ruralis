package com.menard.ruralis.add_places.geocode_model

import android.location.Location
import com.menard.ruralis.utils.GooglePlacesAPI

class GeocodeRepository {

    private val retrofit = GooglePlacesAPI.retrofit.create(GooglePlacesAPI::class.java)

    suspend fun getLatLng(address: String, countryCode: String, key: String): Location? {
        val location = Location("")
        val result = retrofit.getLatLng(address, countryCode, key).results
        if(result != null) {
            val geometry = result[0].geometry
            if (geometry != null) {
                location.latitude = geometry.location?.lat!!
                location.longitude = geometry.location?.lng!!
            }
        }else{
            return null
        }
        return location
    }
}