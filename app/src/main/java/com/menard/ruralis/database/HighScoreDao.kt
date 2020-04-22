package com.menard.ruralis.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.menard.ruralis.quiz.HighScore

@Dao
interface HighScoreDao {

    @Query("SELECT * FROM HighScore")
    suspend fun getAllHighScore(): List<HighScore>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addHighScore(highScore: HighScore): Long
}