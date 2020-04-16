package com.menard.ruralis.search_places

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.menard.ruralis.R
import java.util.*
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
        holder.bind(data[position], onItemClickListener)
    }

    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        var name = itemView.findViewById<TextView>(R.id.item_name)
        var photo = itemView.findViewById<ImageView>(R.id.item_photo)
        var type = itemView.findViewById<TextView>(R.id.item_type)
        var distance = itemView.findViewById<TextView>(R.id.item_distance)
        var progress = itemView.findViewById<ProgressBar>(R.id.list_item_progress)

        fun bind(placeForList: PlaceForList, onItemClickListener: OnItemClickListener) {
            progress.visibility = View.VISIBLE
            name.text = placeForList.name
            type.text = placeForList.type
            if(placeForList.fromRuralis){
                itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.items_from_ruralis))
                if (placeForList.photos != null){
                    Glide.with(itemView.context).load(Uri.parse(placeForList.photos)).centerCrop().error(R.drawable.no_image_available_64).into(photo)
                }else {
                    Glide.with(itemView.context).load(R.drawable.no_image_available_64).error(R.drawable.no_image_available_64).into(photo)
                }
            }else{
                itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.items_from_maps))
                if (placeForList.photos != null) {
                    val photoUrl = itemView.context.getString(R.string.photos_list_view, placeForList.photos, itemView.context.getString(R.string.api_key_google))
                    Glide.with(itemView.context).load(photoUrl).centerCrop().error(R.drawable.no_image_available_64).into(photo)
                } else {
                    Glide.with(itemView.context).load(R.drawable.no_image_available_64).error(R.drawable.no_image_available_64).into(photo)
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