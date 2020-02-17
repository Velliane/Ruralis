package com.menard.ruralis.search_places

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.menard.ruralis.R
import com.menard.ruralis.add_places.Place
import com.menard.ruralis.search_places.textsearch_model.Result

class ListAdapter(private val context: Context): RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    private var data: List<Place> = ArrayList()
    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_list, parent, false)
        mContext = context

        return ListViewHolder(view)
    }

    fun setData(newData: List<Place>){
        data = newData
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(data[position])
    }



    inner class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        var name = itemView.findViewById<TextView>(R.id.item_name)
        var photo = itemView.findViewById<ImageView>(R.id.item_photo)
        var distance = itemView.findViewById<TextView>(R.id.item_distance)

        fun bind(place: Place) {
            name.text = place.name
//            if (result.photos != null) {
//                val photoUrl = context.getString(R.string.photos_list_view,result.photos?.get(0)?.photoReference, context.getString(R.string.api_key_google))
//                Glide.with(context).load(photoUrl).into(photo)
//            }
        }
    }


}