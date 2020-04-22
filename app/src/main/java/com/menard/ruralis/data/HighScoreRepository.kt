package com.menard.ruralis.data

import com.menard.ruralis.database.HighScoreDao
import com.menard.ruralis.quiz.HighScore

class HighScoreRepository(private val highScoreDao: HighScoreDao) {

    suspend fun saveHighScore(highScore: HighScore){
        highScoreDao.addHighScore(highScore)
    }

    suspend fun getAllHighScore(): List<HighScore>{
        return highScoreDao.getAllHighScore()
    }
}