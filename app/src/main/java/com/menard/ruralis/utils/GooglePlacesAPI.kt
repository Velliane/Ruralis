package com.menard.ruralis.utils

import com.menard.ruralis.search_places.details_model.DetailsRequest
import com.menard.ruralis.search_places.textsearch_model.TextSearch
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GooglePlacesAPI {

    @GET("maps/api/place/textsearch/json?types=food&")
    fun getTextSearch(@Query("location") location: String, @Query("radius") radius: String, @Query("query") query: String, @Query("key") key:String): Call<TextSearch>

    @GET("maps/api/place/details/json?")
    fun getDetailsById(@Query("place_id") id: String, @Query("fields") fields: String, @Query("key") key: String): Call<DetailsRequest>

    companion object{
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}