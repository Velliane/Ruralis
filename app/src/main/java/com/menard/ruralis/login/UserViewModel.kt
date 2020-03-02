package com.menard.ruralis.login

import androidx.lifecycle.ViewModel

class UserViewModel: ViewModel() {

    private val userHelper =  UserHelper()

    fun createUser(userId: String, name: String, email:String, photo: String) {
        userHelper.createUser(userId, name, email, photo)
    }
}