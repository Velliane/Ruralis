package com.menard.ruralis.knowsit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.menard.ruralis.R
import com.menard.ruralis.search_places.ListAdapter
import com.menard.ruralis.search_places.PlaceForList

class FavoritesAdapter(private val context: Context) : RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {

    private var data: List<PlaceForList> = ArrayList()
    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_favorites, parent, false)
        mContext = context
        return FavoritesViewHolder(view)
    }

    fun setData(newData: List<PlaceForList>){
        data = newData
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.bind(data[position])
    }

    class FavoritesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        private val favPhoto = itemView.findViewById<ImageView>(R.id.favorites_photo)
        private val favName = itemView.findViewById<TextView>(R.id.favorites_name)

        fun bind(placeForList: PlaceForList){
            if (placeForList.photos != null) {
                val photoUrl = itemView.context.getString(R.string.photos_list_view, placeForList.photos[0], itemView.context.getString(R.string.api_key_google))
                Glide.with(itemView.context).load(photoUrl).error(R.drawable.no_image_available_64).into(favPhoto)
            } else {
                Glide.with(itemView.context).load(R.drawable.no_image_available_64).error(R.drawable.no_image_available_64).into(favPhoto)
            }
            favName.text = placeForList.name
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(id: String, from: Boolean)
    }
}