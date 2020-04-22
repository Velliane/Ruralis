package com.menard.ruralis.quiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.threetenabp.AndroidThreeTen
import com.menard.ruralis.R
import com.menard.ruralis.utils.Constants
import com.menard.ruralis.utils.Injection
import com.menard.ruralis.utils.parseLocalDateTimeToString
import kotlinx.android.synthetic.main.activity_quiz_home.*
import org.threeten.bp.LocalDateTime

class QuizHomeActivity : AppCompatActivity(), View.OnClickListener {

    private var score: Int = 0
    private lateinit var viewModel: QuizHomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_home)
        AndroidThreeTen.init(this)
        start_quiz_btn.setOnClickListener(this)

        val viewModelFactory = Injection.provideViewModelFactory(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(QuizHomeViewModel::class.java)

        val adapter = HighScoreAdapter(this)
        quiz_score_list.adapter = adapter
        quiz_score_list.layoutManager = LinearLayoutManager(this)
        viewModel.getAllHighScore().observe(this, Observer {
            adapter.setData(it)
        })

    }

    override fun onClick(view: View?) {
        when(view){
            start_quiz_btn -> startActivityForResult(Intent(this, QuizActivity::class.java), Constants.RC_QUIZ)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data == null){
            return
        }else{
            score = data.getIntExtra(Constants.INTENT_SCORE, 0)
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("Résultat")
            alertDialog.setMessage("Vous avez obtenu un résultat de $score/10. Saisissez un nom puis validez pour sauvegarder votre score")
            val textView = TextView(this)
            val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            textView.layoutParams = lp
            alertDialog.setView(textView)
            alertDialog.setPositiveButton("Valider") { dialog, _ ->
                val player: String = if(textView.text != ""){
                    textView.text.toString()
                }else{
                    "Player"
                }
                val highScore = HighScore(LocalDateTime.now().toString(), parseLocalDateTimeToString(
                    LocalDateTime.now()), score.toString(), player)
                viewModel.saveHighScore(highScore)
                dialog.dismiss()
                restartActivity()
            }
            alertDialog.setNegativeButton("Annuler"){ dialog, which ->
                dialog.dismiss()
            }
            alertDialog.create().show()
        }
    }

    private fun restartActivity(){
        finish()
        startActivity(intent)
        overridePendingTransition(0,0)
    }
}
