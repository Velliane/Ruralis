package com.menard.ruralis.quiz

data class Question (
    val question: String = "",
    val choice1: String = "",
    val choice2: String = "",
    val choice3: String = "",
    val answerId: Int = 0,
    val questionId: Int = 0,
    val info: String = ""
)