package com.menard.ruralis.utils

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

fun getKnowsItCollection(): CollectionReference {
    return FirebaseFirestore.getInstance().collection(Constants.COLLECTION_KNOWSIT)
}

fun getAllIds(): Task<DocumentSnapshot> {
    return getKnowsItCollection().document("0KnowsItId").get()
}

fun getKnowsIt(id: String): Task<DocumentSnapshot> {
    return getKnowsItCollection().document(id).get()
}