package com.menard.ruralis.search_places

import androidx.lifecycle.MutableLiveData
import com.menard.ruralis.search_places.textsearch_model.TextSearch
import com.menard.ruralis.utils.GooglePlacesAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TextSearchRepository{

    private val retrofit = GooglePlacesAPI.retrofit.create(GooglePlacesAPI::class.java)

    fun getTextSearch(location:String, radius: String, query: String, key: String): MutableLiveData<TextSearch> {

        val data = MutableLiveData<TextSearch>()
        retrofit.getTextSearch(location, radius, query, key).enqueue(object : Callback<TextSearch> {
            override fun onResponse(call: Call<TextSearch>, response: Response<TextSearch>) {
                if(response.isSuccessful){
                    data.value = response.body()
                }
            }
            override fun onFailure(call: Call<TextSearch>, t: Throwable) {
                data.value = null
            }

        })
        return data
    }
}