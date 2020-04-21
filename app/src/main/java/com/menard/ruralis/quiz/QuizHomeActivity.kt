package com.menard.ruralis.quiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.menard.ruralis.R
import com.menard.ruralis.utils.Constants
import kotlinx.android.synthetic.main.activity_quiz_home.*

class QuizHomeActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_home)

        start_quiz_btn.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view){
            start_quiz_btn -> startActivityForResult(Intent(this, QuizActivity::class.java), Constants.RC_QUIZ)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}
