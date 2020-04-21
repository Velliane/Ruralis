package com.menard.ruralis.knowsit

import androidx.lifecycle.*
import com.menard.ruralis.data.*
import com.menard.ruralis.details.Favorite
import com.menard.ruralis.login.User
import com.menard.ruralis.search_places.PlaceForList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(private val connectivityRepository: ConnectivityRepository, private val favoritesDataRepository: FavoritesDataRepository, private val knowsItRepository: KnowsItRepository, private val googleApiRepository: GoogleApiRepository, private val firestoreDataRepository: FirestoreDataRepository): ViewModel() {

    private val randomKnowsIt = MutableLiveData<KnowsIt>()
    private val listFavoritesLiveData = MutableLiveData<List<Favorite>>()
    private val userLiveData = MutableLiveData<User>()
    private val connectionLiveData = connectivityRepository.connectivityLiveData

    val homeLiveData = MediatorLiveData<KnowsIt>()

    init {
        getRandomKnowsIt()
        homeLiveData.addSource(randomKnowsIt, Observer {
            mergeData(it, connectionLiveData.value)
        })
        homeLiveData.addSource(connectionLiveData, Observer {
            mergeData(randomKnowsIt.value, it)
        })
    }

    private fun mergeData(knowsIt: KnowsIt?, connection: Boolean?) {
        if(knowsIt == null || connection == null){
            return
        }else if (connection == false){
            homeLiveData.value = null
        }else{
            homeLiveData.value = knowsIt
        }
    }

    /**
     * Get a random KnowsIt from Firestore
     */
    fun getRandomKnowsIt(){
        viewModelScope.launch {
            randomKnowsIt.value = knowsItRepository.getRandomKnowsIt()
        }
    }

    /**
     * Get all favorites from RoomDatabase
     */
    fun showAllFavorites(): LiveData<List<Favorite>>{
        viewModelScope.launch(Dispatchers.IO) {
            val list = favoritesDataRepository.getAllFavorites()
            withContext(Dispatchers.Main){
                listFavoritesLiveData.value = list
            }
        }
        return listFavoritesLiveData
    }

    /**
     * Add User to LiveData to update Header if connected to internet
     */
    fun updateHeader(displayName: String, photoUrl: String, email: String): LiveData<User> {
        val user = User()
        if (photoUrl != "") {
            user.photo = photoUrl
        }
        user.name = displayName
        user.email = email
        userLiveData.value = user
        return userLiveData
    }

}