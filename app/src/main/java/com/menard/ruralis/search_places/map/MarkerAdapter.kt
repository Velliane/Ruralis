package com.menard.ruralis.search_places.map

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.menard.ruralis.R

class MarkerAdapter(private val layoutInflater: LayoutInflater, private val context: Context): GoogleMap.InfoWindowAdapter {
    @SuppressLint("InflateParams")
    override fun getInfoContents(marker: Marker?): View {
        val view = layoutInflater.inflate(R.layout.marker_custom_layout, null)

        val title = view.findViewById<TextView>(R.id.title)
        val snippet = view.findViewById<TextView>(R.id.snippet)
        //val photo = view.findViewById<ImageView>(R.id.photo)
        title.text = marker?.title
        snippet.text = marker?.snippet
//        val tag = marker?.tag as MarkerTag
//
//        val uri = tag.photoUri
//        if(uri != null){
//            Glide.with(context).load(uri).into(photo)
//        }

        return view
    }

    override fun getInfoWindow(p0: Marker?): View? {
        return null
    }

}