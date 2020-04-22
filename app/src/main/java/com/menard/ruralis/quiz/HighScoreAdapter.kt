package com.menard.ruralis.quiz

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.menard.ruralis.R

class HighScoreAdapter(private val context: Context): RecyclerView.Adapter<HighScoreAdapter.HighScoreViewHolder>() {

    private var data: List<HighScore> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HighScoreViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_highscore, parent, false)
        return HighScoreViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: HighScoreViewHolder, position: Int) {
        holder.bind(data[position])
    }

    fun setData(newData: List<HighScore>){
        data = newData
        notifyDataSetChanged()
    }

    class HighScoreViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        private val date = itemView.findViewById<TextView>(R.id.high_score_date)
        private val score = itemView.findViewById<TextView>(R.id.high_score)
        private val user = itemView.findViewById<TextView>(R.id.high_score_user)

        fun bind(highScore: HighScore){
            date.text = highScore.date
            val scoreTxt = highScore.score
            score.text = "$scoreTxt/10"
            user.text = highScore.player
        }
    }
}