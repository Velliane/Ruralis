package com.menard.ruralis.details.photos

import android.content.Context
import android.net.Uri
import androidx.annotation.NonNull
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.menard.ruralis.data.FirestoreDataRepository
import com.menard.ruralis.data.GoogleApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class PhotosViewModel(private val firestoreDataRepository: FirestoreDataRepository) : ViewModel() {

    val listPhotosLiveData = MediatorLiveData<List<Photo>>()
    private val allPhotosLiveData = MutableLiveData<List<Photo>>()
    private val isSelectedLiveData = MutableLiveData<String>()
    val progressUploadLiveData = MutableLiveData<Int>()

    init {
        listPhotosLiveData.addSource(allPhotosLiveData, Observer {
            combineLiveData(it, isSelectedLiveData.value)
        })
        listPhotosLiveData.addSource(isSelectedLiveData, Observer {
            combineLiveData(allPhotosLiveData.value, it)
        })
    }

    private fun combineLiveData(list: List<Photo>?, uriSelected: String?) {
        if (list == null) {
            return
        }
        val listOfPhotosForRecyclerView = list.map {
            Photo(
                it.uri,
                it.name,
                it.uri == uriSelected
            )
        }
        listPhotosLiveData.value = listOfPhotosForRecyclerView
    }


    fun updatePhotosOfPlace(id: String, photoUri: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val name = UUID.randomUUID().toString()
            var url = ""
            val storageReference = FirebaseStorage.getInstance().getReference("/images/$id/$name")
            val uploadTask = storageReference.putFile(Uri.parse(photoUri))
            uploadTask.addOnProgressListener {
                val progress: Double = 100.0 * it.bytesTransferred / it.totalByteCount
                progressUploadLiveData.value = progress.toInt()
            }.addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener {uri->
                    url = uri.toString()
                    firestoreDataRepository.savePhoto(id, name, url)
                }
            }
        }
    }


    fun getAllPhotosAccordingOrigin(fromRuralis: Boolean, id: String, listUri: List<String?>, context: Context) {
        if (fromRuralis) {
            viewModelScope.launch(Dispatchers.IO) {
                val list = firestoreDataRepository.getListOfPhotosFromFirestore(id, context)
                withContext(Dispatchers.Main) {
                    allPhotosLiveData.value = list
                }
            }
        } else {
            val listFromMaps = ArrayList<Photo>()
            listUri.forEach {
                val name = UUID.randomUUID().toString()
                val photo =
                    Photo(it!!, name, false)
                listFromMaps.add(photo)
            }
            allPhotosLiveData.value = listFromMaps
        }
    }

    fun photoClicked(uri: String?) {
        isSelectedLiveData.value = uri
    }

}