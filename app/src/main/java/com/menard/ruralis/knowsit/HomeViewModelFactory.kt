package com.menard.ruralis.knowsit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.menard.ruralis.data.KnowsItRepository
import java.lang.IllegalArgumentException

class HomeViewModelFactory(private val knowsItRepository: KnowsItRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel(
               knowsItRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }

}