package com.menard.ruralis.quiz

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity

data class HighScore (

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = "",

    @ColumnInfo(name = "date")
    val date: String = "",
    @ColumnInfo(name = "score")
    val score: String = "",
    @ColumnInfo(name = "player")
    val player: String = ""
)