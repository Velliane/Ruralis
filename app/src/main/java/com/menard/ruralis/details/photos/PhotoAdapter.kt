package com.menard.ruralis.details.photos

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.menard.ruralis.R

class PhotoAdapter(private val context: Context, private val fromRuralis: Boolean, private val listener: OnItemClickListener): RecyclerView.Adapter<PhotoAdapter.PhotosViewHolder>() {

    private var data: List<Photo> = ArrayList()
    private lateinit var mContext: Context
    private lateinit var onItemClickListener: OnItemClickListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_photo, parent, false)
        mContext = context
        onItemClickListener = listener
        return PhotosViewHolder(view)
    }

    fun setData(newData: List<Photo>) {
        data = newData
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        holder.bind(data[position], onItemClickListener)
    }

    inner class PhotosViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val image: ImageView = itemView.findViewById(R.id.item_list_photo)
        val container: ConstraintLayout = itemView.findViewById(R.id.photo_container)

        fun bind(photo: Photo, onItemClickListener: OnItemClickListener){
            if(fromRuralis) {
                Glide.with(context).load(Uri.parse(photo.uri)).centerCrop().error(R.drawable.no_image_available_64).into(image)
            }else{
                val photoUrl = itemView.context.getString(R.string.photos_list_view, photo.uri, itemView.context.getString(R.string.api_key_google))
                Glide.with(itemView.context).load(photoUrl).centerCrop().error(R.drawable.no_image_available_64).into(image)
            }
            if(photo.selected!!){
                container.setBackgroundResource(R.color.colorAccent)
            }else{
                container.setBackgroundResource(R.color.quantum_white_100)
            }
            itemView.setOnClickListener{
                onItemClickListener.onItemClicked(photo.uri)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(uri: String?)
    }

}