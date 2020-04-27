package com.menard.ruralis.utils


class Constants {

    companion object {

        const val QUERY_GM = "producteur"

        //-- REQUEST CODE --//
        const val RC_FINE_LOCATION = 1
        const val RC_LOGIN = 2
        const val REQUEST_CODE_CALL_PHONE = 3
        const val RC_QUIZ = 4

        //-- FIRESTORE COLLECTION --//
        const val COLLECTION_KNOWS_IT = "knowsit"
        const val COLLECTION_USERS = "users"
        const val COLLECTION_PLACES = "places"
        const val COLLECTION_COMMENTS = "comments"
        const val COLLECTION_PHOTOS = "photos"
        const val COLLECTION_QUIZ = "quiz"

        //-- SHARED PREFERENCES --//
        const val SHARED_PREFERENCES = "SHARED_PREFERENCES"
        const val PREF_ID_PLACE = "PREF_ID_PLACE"
        const val PREF_SEARCH_FROM_MAPS = "PREF_SEARCH_FROM_MAPS"
        const val PREF_SEARCH_AROUND = "PREF_SEARCH_AROUND"
        const val PREF_GUIDE_HOME = "PREF_GUIDE_HOME"

        //-- INTENT --//
        const val INTENT_ID = "INTENT_ID"
        const val INTENT_FROM = "INTENT_FROM"
        const val INTENT_URI = "INTENT_URI"
        const val INTENT_EDIT = "INTENT_EDIT"
        const val INTENT_SCORE= "INTENT_SCORE"
    }
}