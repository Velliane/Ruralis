package com.menard.ruralis.quiz

data class Question (
    val question: String,
    val choice1: String,
    val choice2: String,
    val choice3: String,
    val answerId: Int,
    val questionId: Int,
    val info: String
)