package com.menard.ruralis.utils

import android.graphics.Color
import androidx.core.graphics.ColorUtils

class Constants {

    companion object {

        //-- REQUEST CODE --//
        const val RC_FINE_LOCATION = 1
        const val RC_LOGIN = 2
        const val REQUEST_CODE_CALL_PHONE = 3

        //-- FIRESTORE COLLECTION --//
        const val COLLECTION_KNOWS_IT = "knowsit"
        const val COLLECTION_USERS = "users"
        const val COLLECTION_PLACES = "places"
        const val COLLECTION_COMMENTS = "comments"

        //-- SHARED PREFERENCES --//
        const val SHARED_PREFERENCES = "SHARED_PREFERENCES"
        const val PREF_ID_PLACE = "PREF_ID_PLACE"
        const val PREF_FAVORITES = "PREF_FAVORITES"
        const val PREF_PRIMARY_COLOR = "PREF_PRIMARY_COLOR"
        const val PREF_ACCENT_COLOR = "PREF_ACCENT_COLOR"

        //-- INTENT --//
        const val INTENT_ID = "INTENT_ID"
        const val INTENT_FROM = "INTENT_FROM"
        const val INTENT_URI = "INTENT_URI"

        //-- COLOR VALUE --//
        const val COLOR_PRIMARY_DEFAULT = -10504866
        const val COLOR_ACCENT_DEFAULT = -6527435
    }
}