package com.menard.ruralis.quiz

import androidx.lifecycle.*
import com.menard.ruralis.data.ConnectivityRepository
import com.menard.ruralis.data.FirestoreDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class QuizViewModel(private val firestoreDataRepository: FirestoreDataRepository, private val connectivityRepository: ConnectivityRepository): ViewModel() {

    private val connectivityLiveData = connectivityRepository.connectivityLiveData
    private val questionsListLiveData = MutableLiveData<List<Question?>>()
    private val quizLiveData = MediatorLiveData<List<Question?>>()

    val progressLiveData = MutableLiveData<Boolean>()
    val _quizLiveData: LiveData<List<Question?>> = quizLiveData


    init {
        progressLiveData.value = false
        quizLiveData.addSource(questionsListLiveData, Observer {
            mergeData(connectivityLiveData.value, it)
        })
        quizLiveData.addSource(connectivityLiveData, Observer {
            mergeData(it, questionsListLiveData.value)
        })
    }

    private fun mergeData(connectivity: Boolean?, listQuestions: List<Question?>?) {
        if (connectivity == false) {
            return
        } else if (connectivity == true && listQuestions == null){
            return
        }else{
            quizLiveData.value = listQuestions
        }
    }

    fun getListOfQuestion(listId: List<Int>){
        viewModelScope.launch(Dispatchers.IO) {
            val list = firestoreDataRepository.getListOfQuestion(listId)
            withContext(Dispatchers.Main){
                questionsListLiveData.value = list
                progressLiveData.value = true
            }
        }
    }

    fun generateId(): List<Int>{
        val listId = ArrayList<Int>()
        val max = 12
        val min = 1
        while (listId.size <= 10){
            val id = Random.nextInt((max-min)+1)+min
            listId.add(id)
            listId.distinct()
        }
        return listId
    }
}