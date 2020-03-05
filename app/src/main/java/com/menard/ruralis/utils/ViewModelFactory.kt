package com.menard.ruralis.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.menard.ruralis.add_places.AddViewModel
import com.menard.ruralis.data.FirestoreDataRepository
import com.menard.ruralis.data.GoogleApiRepository
import com.menard.ruralis.data.KnowsItRepository
import com.menard.ruralis.details.comments.CommentsViewModel
import com.menard.ruralis.details.DetailsViewModel
import com.menard.ruralis.knowsit.HomeViewModel
import com.menard.ruralis.login.UserViewModel
import com.menard.ruralis.search_places.MainViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory(private val googleApiRepository: GoogleApiRepository, private val firestoreDataRepository: FirestoreDataRepository, private val knowsItRepository: KnowsItRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                return MainViewModel(
                    googleApiRepository, firestoreDataRepository
                ) as T
            }
            modelClass.isAssignableFrom(DetailsViewModel::class.java) -> {
                return DetailsViewModel(
                    googleApiRepository, firestoreDataRepository
                ) as T
            }
            modelClass.isAssignableFrom(AddViewModel::class.java) -> {
                return AddViewModel(
                    firestoreDataRepository
                ) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                return HomeViewModel(
                    knowsItRepository
                ) as T
            }
            modelClass.isAssignableFrom(UserViewModel::class.java) -> {
                return UserViewModel(
                ) as T
            }
            modelClass.isAssignableFrom(CommentsViewModel::class.java) -> {
                return CommentsViewModel(
                    firestoreDataRepository,
                    googleApiRepository
                ) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel")
        }
    }

}