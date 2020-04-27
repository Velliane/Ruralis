package com.menard.ruralis.search_places

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.menard.ruralis.R
import com.menard.ruralis.utils.getProgressDrawableSpinner
import com.menard.ruralis.utils.loadPlacePhoto
import kotlin.collections.ArrayList

class ListAdapter(private val listener: OnItemClickListener, private val context: Context): RecyclerView.Adapter<ListAdapter.ListViewHolder>(){

    private var data: List<PlaceForList> = ArrayList()
    private lateinit var onItemClickListener: OnItemClickListener
    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_list, parent, false)
        onItemClickListener = listener
        mContext = context
        return ListViewHolder(view)
    }

    fun setData(newData: List<PlaceForList>){
        data = newData
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(data[position], onItemClickListener, mContext)
    }

    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        var name: TextView = itemView.findViewById(R.id.item_name)
        var photo: ImageView = itemView.findViewById(R.id.item_photo)
        var type: TextView = itemView.findViewById(R.id.item_type)
        var address: TextView = itemView.findViewById(R.id.item_address)
        var progress: ProgressBar = itemView.findViewById(R.id.list_item_progress)
        var openNowLogo: ImageView = itemView.findViewById(R.id.item_logo)

        fun bind(placeForList: PlaceForList, onItemClickListener: OnItemClickListener, context: Context) {
            progress.visibility = View.VISIBLE
            name.text = placeForList.name
            type.text = placeForList.type
            address.text = placeForList.address
            if(placeForList.openNow != null){
                if(placeForList.openNow){
                    Glide.with(itemView.context).load(R.drawable.goat_open).into(openNowLogo)
                }else{
                    Glide.with(itemView.context).load(R.drawable.goat_closed).into(openNowLogo)
                }
            }

            if(placeForList.fromRuralis){
                itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.items_from_ruralis))
                if (placeForList.photos != null){
                    photo.loadPlacePhoto(placeForList.photos, null, getProgressDrawableSpinner(context))
                }else {
                    photo.loadPlacePhoto(null, R.drawable.no_image_available_64, getProgressDrawableSpinner(context))
                }
            }else{
                itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.items_from_maps))
                if (placeForList.photos != null) {
                    val photoUrl = itemView.context.getString(R.string.photos_list_view, placeForList.photos, itemView.context.getString(R.string.api_key_google))
                    photo.loadPlacePhoto(photoUrl, R.drawable.no_image_available_64, getProgressDrawableSpinner(context))
                } else {
                    photo.loadPlacePhoto(null, R.drawable.no_image_available_64, getProgressDrawableSpinner(context))
                }
            }
            progress.visibility = View.GONE
            itemView.setOnClickListener {
                onItemClickListener.onItemClicked(placeForList.placeId, placeForList.fromRuralis,
                    placeForList.photos
                )
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(id: String, from: Boolean, photo: String?)
    }


}