package com.menard.ruralis.add_places

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.menard.ruralis.R

class OpeningsListAdapter(private val context: Context, private val listener: OnItemClickListener, private val edit: Boolean): RecyclerView.Adapter<OpeningsListAdapter.OpeningsViewHolder>(){

    private var data: List<String> = ArrayList()
    private lateinit var onItemClickListener: OnItemClickListener
    private var mEdit: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OpeningsViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_opening_hours_preview, parent, false)
        onItemClickListener = listener
        mEdit = edit
        return OpeningsViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: OpeningsViewHolder, position: Int) {
        holder.bind(data[position], onItemClickListener, edit)
    }

    fun setData(newData: List<String>) {
        data = newData
        notifyDataSetChanged()
    }


    class OpeningsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val openingPreview = itemView.findViewById<TextView>(R.id.item_openings_preview_text)
        private val deleteBtn = itemView.findViewById<ImageView>(R.id.remove_openings)

        fun bind(
            opening: String,
            onItemClickListener: OnItemClickListener, edit: Boolean
        ){
            openingPreview.text = opening
            if (edit){
                deleteBtn.visibility = View.VISIBLE
            }else{
                deleteBtn.visibility = View.INVISIBLE
            }
            deleteBtn.setOnClickListener {
                onItemClickListener.onItemClicked(opening)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(opening: String)
    }

}