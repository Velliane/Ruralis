package com.menard.ruralis.quiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import cn.refactor.lib.colordialog.PromptDialog
import com.menard.ruralis.R
import com.menard.ruralis.utils.Constants
import com.menard.ruralis.utils.Injection
import kotlinx.android.synthetic.main.activity_quiz.*

class QuizActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var viewModel: QuizViewModel
    private lateinit var listOfQuestion: List<Question?>
    private var questionIndex = 0
    private var score = 0
    private var currentQuestion: Question? = null

    private val progressBar = CustomProgressBar()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        choice_one_btn.tag = 1
        choice_one_btn.setOnClickListener(this)
        choice_two_btn.tag = 2
        choice_two_btn.setOnClickListener(this)
        choice_three_btn.tag = 3
        choice_three_btn.setOnClickListener(this)

        val viewModelFactory = Injection.provideViewModelFactory(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(QuizViewModel::class.java)

        progressBar.show(this, getString(R.string.loading_questions))
        getListOfQuestion()
    }

    private fun getListOfQuestion(){
        val listOfId = viewModel.generateId()
        viewModel.getListOfQuestion(listOfId)
        viewModel._quizLiveData.observe(this, Observer {
            listOfQuestion = it
            showQuestion()
        })
        viewModel.progressLiveData.observe(this, Observer {
            if (it){
                progressBar.dialog.dismiss()
            }
        })
    }

    private fun showQuestion() {
        val question = listOfQuestion[questionIndex]
        currentQuestion = question
        if (question != null) {
            quiz_question.text = question.question
            choice_one_btn.text = question.choice1
            choice_two_btn.text = question.choice2
            choice_three_btn.text = question.choice3
        }
    }

    override fun onClick(view: View?) {
        val answerId = view?.tag as Int
        if (answerId == currentQuestion?.answerId) {
            showPromptDialog(getString(R.string.good_answer), PromptDialog.DIALOG_TYPE_SUCCESS)
        } else {
            showPromptDialog(getString(R.string.bad_answer), PromptDialog.DIALOG_TYPE_WRONG)
        }
    }

    private fun showPromptDialog(result: String, type: Int){
        val dialog = PromptDialog(this)
        dialog.setTitleText(result)
        dialog.setContentText(currentQuestion?.info)
        dialog.setAnimationEnable(true)
        dialog.setCancelable(false)
        dialog.dialogType = type
        dialog.setPositiveListener(getString(R.string.next_question), PromptDialog.OnPositiveListener {
            questionIndex++
            if (type == PromptDialog.DIALOG_TYPE_SUCCESS){
                score++
            }
            if (questionIndex == listOfQuestion.size) {
                val intent = Intent()
                intent.putExtra(Constants.INTENT_SCORE, score)
                setResult(Constants.RC_QUIZ, intent)
                finish()
            } else {
                showQuestion()
                dialog.dismiss()
            }
        })
        dialog.show()
    }

}
