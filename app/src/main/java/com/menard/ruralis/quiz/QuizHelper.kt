package com.menard.ruralis.quiz
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.menard.ruralis.utils.Constants
import kotlinx.coroutines.tasks.await

class QuizHelper {

    private fun getQuizCollection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(Constants.COLLECTION_QUIZ)
    }

    suspend fun getQuestionsForQuiz(id: Int): QuerySnapshot? {
        return getQuizCollection().whereEqualTo("questionId", id).get().await()
    }
}