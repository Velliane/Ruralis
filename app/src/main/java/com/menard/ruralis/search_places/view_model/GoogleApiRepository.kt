package com.menard.ruralis.search_places.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.menard.ruralis.add_places.Place
import com.menard.ruralis.search_places.details_model.DetailsRequest
import com.menard.ruralis.search_places.details_model.ResultDetails
import com.menard.ruralis.search_places.textsearch_model.TextSearch
import com.menard.ruralis.utils.GooglePlacesAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.ln

class GoogleApiRepository{

    private val retrofit = GooglePlacesAPI.retrofit.create(GooglePlacesAPI::class.java)

    fun getTextSearch(location:String, radius: String, query: String, key: String): LiveData<ArrayList<Place>> {

        val data = MutableLiveData<ArrayList<Place>>()
        retrofit.getTextSearch(location, radius, query, key).enqueue(object : Callback<TextSearch> {
            override fun onResponse(call: Call<TextSearch>, response: Response<TextSearch>) {
                if(response.isSuccessful){
                    val list = ArrayList<Place>()
                    val textSearch = response.body()
                    val listResult = textSearch!!.results
                    for(result in listResult!!){
                        val lat = result.geometry!!.location!!.lat.toString()
                        val lng = result.geometry!!.location!!.lng.toString()
                        val place = Place(result.placeId.toString(), result.types!![0], result.name.toString(), result.formattedAddress.toString(), lat, lng, false)
                        list.add(place)
                    }
                    data.value = list
                }
            }
            override fun onFailure(call: Call<TextSearch>, t: Throwable) {
               //
            }

        })
        return data
    }

    fun getDetails(place_id: String, fields: String, key: String): LiveData<Place> {
        val detailed = MutableLiveData<Place>()
        retrofit.getDetailsById(place_id, fields, key).enqueue(object : Callback<DetailsRequest> {
            override fun onFailure(call: Call<DetailsRequest>, t: Throwable) {
                //detailed.value = null
            }

            override fun onResponse(call: Call<DetailsRequest>, response: Response<DetailsRequest>) {
               if(response.isSuccessful){
                   val result = response.body()!!.result!!
                   val lat = result.geometry!!.location!!.lat.toString()
                   val lng = result.geometry!!.location!!.lng.toString()
                   val place = Place(result.placeId.toString(), "Etablissement trouvé sur GoogleMap", result.name.toString(), result.vicinity.toString(), lat, lng, false)
                    detailed.value = place
               }
            }
        })
        return detailed
    }
}

