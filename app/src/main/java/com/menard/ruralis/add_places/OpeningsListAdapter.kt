package com.menard.ruralis.add_places

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.menard.ruralis.R

class OpeningsListAdapter(private val context: Context): RecyclerView.Adapter<OpeningsListAdapter.OpeningsViewHolder>(){

    private var data: List<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OpeningsViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_opening_hours_preview, parent, false)
        return OpeningsViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: OpeningsViewHolder, position: Int) {
        holder.bind(data[position])
    }

    fun setData(newData: List<String>) {
        data = newData
        notifyDataSetChanged()
    }


    class OpeningsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val openingPreview = itemView.findViewById<TextView>(R.id.item_openings_preview_text)

        fun bind(opening: String){
            openingPreview.text = opening
        }
    }


}