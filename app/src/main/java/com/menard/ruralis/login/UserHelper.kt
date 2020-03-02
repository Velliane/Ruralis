package com.menard.ruralis.login

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.menard.ruralis.utils.Constants

open class UserHelper {

    private fun getUsersCollection(): CollectionReference {
        val instance = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder().setTimestampsInSnapshotsEnabled(true).build()
        instance.firestoreSettings = settings
        return instance.collection(Constants.COLLECTION_USERS)
    }

    open fun createUser(userId: String, name: String, email: String, photo: String): Task<Void> {
        val newUser = User(userId, name, email, photo)
        return getUsersCollection().document(userId).set(newUser)
    }

    open fun getUser(userId: String): Task<DocumentSnapshot> {
        return getUsersCollection().document(userId).get()
    }
}


