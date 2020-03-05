package com.menard.ruralis.details.comments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.menard.ruralis.data.FirestoreDataRepository
import com.menard.ruralis.data.GoogleApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CommentsViewModel(private val firestoreDataRepository: FirestoreDataRepository, private val googleApiRepository: GoogleApiRepository): ViewModel() {

    val commentsLiveData = MutableLiveData<List<Comments>>()

    fun getCommentsOfPlace(id: String, fromRuralis: Boolean, fields: String, key: String){
        if(fromRuralis) {
            viewModelScope.launch(Dispatchers.IO) {
                val list: List<Comments> = firestoreDataRepository.getCommentsOfPlace(id)
                withContext(Dispatchers.Main) {
                    commentsLiveData.value = list
                }
            }
        }else{
            viewModelScope.launch(Dispatchers.IO) {
                val list = googleApiRepository.getComments(id, fields, key)
                withContext(Dispatchers.Main) {
                    commentsLiveData.value = list
                }
            }
        }
    }
}