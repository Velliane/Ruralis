package com.menard.ruralis.search_places

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.menard.ruralis.R

class ListAdapter(private val listener: OnItemClickListener, private val context: Context): RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

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
        holder.bind(data[position])
    }



    inner class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        var name = itemView.findViewById<TextView>(R.id.item_name)
        var photo = itemView.findViewById<ImageView>(R.id.item_photo)
        var distance = itemView.findViewById<TextView>(R.id.item_distance)

        fun bind(placeForList: PlaceForList) {
            name.text = placeForList.name

            if (placeForList.photos != null) {
                val photoUrl = context.getString(R.string.photos_list_view, placeForList.photos, context.getString(R.string.api_key_google))
                Glide.with(context).load(photoUrl).into(photo)
            }else{
                Glide.with(context).load(R.drawable.no_image_available_64).into(photo)
            }

            if(placeForList.fromRuralis){
                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.items_from_ruralis))
            }else{
                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.items_from_maps))
            }

            itemView.setOnClickListener {
                onItemClickListener.onItemClicked(placeForList.placeId, placeForList.fromRuralis)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(id: String, from: Boolean)
    }

}