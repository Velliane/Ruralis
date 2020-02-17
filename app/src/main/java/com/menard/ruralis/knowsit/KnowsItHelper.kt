package com.menard.ruralis.knowsit

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.menard.ruralis.utils.Constants
import kotlinx.coroutines.tasks.await


open class KnowsItHelper{

    open fun getKnowsItCollection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(Constants.COLLECTION_KNOWSIT)
    }

    open suspend fun getAllIds(): DocumentSnapshot {
        return getKnowsItCollection().document("0KnowsItId").get().await()
    }

    open suspend fun getKnowsIt(id: String): DocumentSnapshot {
        return getKnowsItCollection().document(id).get().await()
    }
}