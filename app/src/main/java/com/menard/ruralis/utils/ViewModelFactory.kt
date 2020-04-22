package com.menard.ruralis.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.menard.ruralis.add_places.AddViewModel
import com.menard.ruralis.add_places.geocode_model.GeocodeRepository
import com.menard.ruralis.data.*
import com.menard.ruralis.details.comments.CommentsViewModel
import com.menard.ruralis.details.DetailsViewModel
import com.menard.ruralis.details.photos.PhotosViewModel
import com.menard.ruralis.knowsit.HomeViewModel
import com.menard.ruralis.login.UserViewModel
import com.menard.ruralis.quiz.QuizHomeActivity
import com.menard.ruralis.quiz.QuizHomeViewModel
import com.menard.ruralis.quiz.QuizViewModel
import com.menard.ruralis.search_places.MainViewModel
import com.menard.ruralis.search_places.list.ListViewModel
import com.menard.ruralis.search_places.map.MapViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory(private val context: Context, private val highScoreRepository: HighScoreRepository, private val connectivityRepository: ConnectivityRepository, private val geocodeRepository: GeocodeRepository, private val favoritesDataRepository: FavoritesDataRepository, private val googleApiRepository: GoogleApiRepository, private val firestoreDataRepository: FirestoreDataRepository, private val knowsItRepository: KnowsItRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                return MainViewModel(
                    context, AuthUI.getInstance()
                ) as T
            }
            modelClass.isAssignableFrom(DetailsViewModel::class.java) -> {
                return DetailsViewModel(
                    context, favoritesDataRepository, googleApiRepository, firestoreDataRepository
                ) as T
            }
            modelClass.isAssignableFrom(AddViewModel::class.java) -> {
                return AddViewModel(
                    firestoreDataRepository, geocodeRepository, context
                ) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                return HomeViewModel(
                    connectivityRepository, favoritesDataRepository, knowsItRepository
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
            modelClass.isAssignableFrom(ListViewModel::class.java) -> {
                return ListViewModel(context, googleApiRepository, firestoreDataRepository) as T
            }
            modelClass.isAssignableFrom(MapViewModel::class.java) -> {
                return MapViewModel(context, googleApiRepository, firestoreDataRepository) as T
            }
            modelClass.isAssignableFrom(PhotosViewModel::class.java) -> {
                return PhotosViewModel(firestoreDataRepository) as T
            }
            modelClass.isAssignableFrom(QuizViewModel::class.java) -> {
                return QuizViewModel(firestoreDataRepository, connectivityRepository) as T
            }
            modelClass.isAssignableFrom(QuizHomeViewModel::class.java) -> {
                return QuizHomeViewModel(highScoreRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel")
        }
    }

}