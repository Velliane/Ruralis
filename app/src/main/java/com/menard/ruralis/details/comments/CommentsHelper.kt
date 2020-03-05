package com.menard.ruralis.details.comments

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.menard.ruralis.utils.Constants
import kotlinx.coroutines.tasks.await

class CommentsHelper {

    private fun getCommentsCollection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(Constants.COLLECTION_COMMENTS)
    }

    open fun addComment(id: String, name: String, text: String): Task<Void> {
        val newComments = Comments(name, text, id)
        return getCommentsCollection().document().set(newComments)
    }

    open suspend fun getAllComments(id: String): QuerySnapshot? {
        return getCommentsCollection().whereEqualTo("id", id).get().await()
    }
}