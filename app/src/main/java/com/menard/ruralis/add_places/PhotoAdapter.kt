package com.menard.ruralis.add_places

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.menard.ruralis.R

class PhotoAdapter(private val context: Context): RecyclerView.Adapter<PhotoAdapter.PhotosViewHolder>() {

    private var data: List<Uri> = ArrayList()
    private lateinit var mContext: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_photo, parent, false)
        mContext = context
        return PhotosViewHolder(view)
    }

    fun setData(newData: List<Uri>) {
        data = newData
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        holder.bind(data[position])
    }

    inner class PhotosViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val image = itemView.findViewById<ImageView>(R.id.item_list_photo)

        fun bind(uri: Uri){
            Glide.with(context).load(uri).centerCrop().into(image)
        }

    }
}