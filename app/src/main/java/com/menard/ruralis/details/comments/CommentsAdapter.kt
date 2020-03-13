package com.menard.ruralis.details.comments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.menard.ruralis.R
import com.menard.ruralis.details.comments.CommentsAdapter.*

class CommentsAdapter (private val context: Context): RecyclerView.Adapter<CommentsViewHolder>(){

    private var data: List<Comments> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_comments, parent, false)
        return CommentsViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
            holder.bind(data[position])

    }

    fun setData(newData: List<Comments>) {
        data = newData
        notifyDataSetChanged()
    }


    class CommentsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val nameUser = itemView.findViewById<TextView>(R.id.item_comments_name)
        private val commentTxt = itemView.findViewById<TextView>(R.id.item_comments_text)

        fun bind(comments: Comments){
            nameUser.text = comments.name
            commentTxt.text = comments.text
            nameUser.tag = comments.id
        }
    }


}