package com.menard.ruralis.search_places

import android.content.Context
import android.content.Intent
import androidx.lifecycle.*
import com.firebase.ui.auth.AuthUI
import com.menard.ruralis.data.FirestoreDataRepository
import com.menard.ruralis.data.GoogleApiRepository
import com.menard.ruralis.login.LoginActivity
import com.menard.ruralis.login.User

class MainViewModel(private val context: Context, private val authUI: AuthUI): ViewModel() {

    private val userLiveData = MutableLiveData<User>()

    fun updateHeader(displayName: String, photoUrl: String, email: String): LiveData<User> {
        val user = User()
            //-- If connected to internet, get user's information from Firebase --//
            if (photoUrl != "") {
                user.photo = photoUrl
            }
            user.name = displayName
            user.email = email
            userLiveData.value = user
        return userLiveData
    }

    //-- LOG OUT --//
    fun logOut(){
        authUI.signOut(context).addOnCompleteListener {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }
}