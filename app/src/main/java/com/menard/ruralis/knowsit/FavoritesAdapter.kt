package com.menard.ruralis.knowsit

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.menard.ruralis.R
import com.menard.ruralis.details.Favorite

class FavoritesAdapter(private val context: Context, private val listener: OnItemClickListener) : RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {

    private var data: List<Favorite> = ArrayList()
    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_favorites, parent, false)
        mContext = context
        return FavoritesViewHolder(view)
    }

    fun setData(newData: List<Favorite>){
        data = newData
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.bind(data[position], listener)
    }

    class FavoritesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        private val favPhoto = itemView.findViewById<ImageView>(R.id.favorites_photo)
        private val favName = itemView.findViewById<TextView>(R.id.favorites_name)
        private val container = itemView.findViewById<ConstraintLayout>(R.id.favorites_container)

        fun bind(favorite: Favorite, listener: OnItemClickListener){
            if(favorite.fromRuralis){
                container.setBackgroundResource(R.color.items_from_ruralis)
                if (favorite.photo != null) {
                    Glide.with(itemView.context).load(Uri.parse(favorite.photo)).centerCrop().error(R.drawable.no_image_available_64).into(favPhoto)
                } else {
                    Glide.with(itemView.context).load(R.drawable.no_image_available_64).error(R.drawable.no_image_available_64).into(favPhoto)
                }
            }else{
                container.setBackgroundResource(R.color.items_from_maps)
                if (favorite.photo != null) {
                    val photoUrl = itemView.context.getString(R.string.photos_list_view, favorite.photo, itemView.context.getString(R.string.api_key_google))
                    Glide.with(itemView.context).load(photoUrl).centerCrop().error(R.drawable.no_image_available_64).into(favPhoto)
                } else {
                    Glide.with(itemView.context).load(R.drawable.no_image_available_64).error(R.drawable.no_image_available_64).into(favPhoto)
                }
            }
            itemView.setOnClickListener{
                listener.onItemClicked(favorite.id, favorite.fromRuralis, favorite.photo)
            }

            favName.text = favorite.name

        }
    }

    interface OnItemClickListener {
        fun onItemClicked(id: String, from: Boolean, photoUri: String?)
    }
}