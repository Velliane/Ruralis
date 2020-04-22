package com.menard.ruralis.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.menard.ruralis.data.HighScoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuizHomeViewModel(private val highScoreRepository: HighScoreRepository): ViewModel() {

    val listHighScoreLiveData = MutableLiveData<List<HighScore>>()

    fun saveHighScore(highScore: HighScore){
        viewModelScope.launch(Dispatchers.IO) {
            highScoreRepository.saveHighScore(highScore)
        }
    }

    fun getAllHighScore(): LiveData<List<HighScore>>{
        viewModelScope.launch(Dispatchers.IO) {
            val list = highScoreRepository.getAllHighScore()
            withContext(Dispatchers.Main){
                listHighScoreLiveData.value = list
            }
        }
        return listHighScoreLiveData
    }
}