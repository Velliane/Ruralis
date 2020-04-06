package com.menard.ruralis.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.menard.ruralis.add_places.AddViewModel
import com.menard.ruralis.add_places.geocode_model.GeocodeRepository
import com.menard.ruralis.data.FavoritesDataRepository
import com.menard.ruralis.data.FirestoreDataRepository
import com.menard.ruralis.data.GoogleApiRepository
import com.menard.ruralis.data.KnowsItRepository
import com.menard.ruralis.details.comments.CommentsViewModel
import com.menard.ruralis.details.DetailsViewModel
import com.menard.ruralis.details.photos.PhotosViewModel
import com.menard.ruralis.knowsit.HomeViewModel
import com.menard.ruralis.login.UserViewModel
import com.menard.ruralis.search_places.MainViewModel
import com.menard.ruralis.search_places.list.ListViewModel
import com.menard.ruralis.search_places.map.MapViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory(private val context: Context, private val geocodeRepository: GeocodeRepository, private val favoritesDataRepository: FavoritesDataRepository, private val googleApiRepository: GoogleApiRepository, private val firestoreDataRepository: FirestoreDataRepository, private val knowsItRepository: KnowsItRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                return MainViewModel(
                    googleApiRepository, firestoreDataRepository
                ) as T
            }
            modelClass.isAssignableFrom(DetailsViewModel::class.java) -> {
                return DetailsViewModel(
                    favoritesDataRepository, googleApiRepository, firestoreDataRepository
                ) as T
            }
            modelClass.isAssignableFrom(AddViewModel::class.java) -> {
                return AddViewModel(
                    firestoreDataRepository, geocodeRepository, context
                ) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                return HomeViewModel(
                    favoritesDataRepository, knowsItRepository, googleApiRepository, firestoreDataRepository
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
            else -> throw IllegalArgumentException("Unknown ViewModel")
        }
    }

}