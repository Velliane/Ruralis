package com.menard.ruralis.knowsit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class HomeViewModel(private val knowsItRepository: KnowsItRepository): ViewModel() {

    val randomKnowsIt = MutableLiveData<KnowsIt>()
    

    fun getRandomKnowsIt(){
        viewModelScope.launch {
            randomKnowsIt.value = knowsItRepository.getRandomKnowsIt()
        }
    }
}