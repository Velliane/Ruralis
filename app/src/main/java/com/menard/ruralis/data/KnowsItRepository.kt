package com.menard.ruralis.data

import com.menard.ruralis.knowsit.KnowsIt
import com.menard.ruralis.knowsit.KnowsItHelper

class KnowsItRepository {

    private val knowsItHelper = KnowsItHelper()

    suspend fun getRandomKnowsIt(): KnowsIt {
        val index = 0
        val documentSnapshot = knowsItHelper.getAllIds()
        val listId: ArrayList<String> = documentSnapshot.get("id") as ArrayList<String>
        listId.shuffle()
        val document = knowsItHelper.getKnowsIt(listId[index])
        return document.toObject(KnowsIt::class.java)!!
    }
}