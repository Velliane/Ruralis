package com.menard.ruralis.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.menard.ruralis.details.Favorite
import kotlin.collections.ArrayList


open class SharedPreference {

    open fun saveFavorites(context: Context, favorites: List<Favorite?>?) {
        val editor: SharedPreferences.Editor
        val settings: SharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        editor = settings.edit()
        val gson = Gson()
        val jsonFavorites = gson.toJson(favorites)
        editor.putString(Constants.PREF_FAVORITES, jsonFavorites)
        editor.apply()
    }

    fun addFavorite(context: Context, favorite: Favorite?) {
        var favorites: MutableList<Favorite?>? = getFavorites(context)
        if (favorites == null) favorites = ArrayList()
        favorites.add(favorite)
        saveFavorites(context, favorites)
    }

    fun removeFavorite(context: Context, favorite: Favorite?) {
        val favorites: ArrayList<Favorite?>? = getFavorites(context)
        if (favorites != null) {
            favorites.remove(favorite)
            saveFavorites(context, favorites)
        }
    }

    open fun getFavorites(context: Context): ArrayList<Favorite?>? {
        var favorites: List<Favorite?>?
        val settings: SharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        if (settings.contains(Constants.PREF_FAVORITES)) {
            val jsonFavorites = settings.getString(Constants.PREF_FAVORITES, null)
            val gson = Gson()
            val favoriteItems: Array<Favorite> = gson.fromJson(jsonFavorites, Array<Favorite>::class.java)
            favorites = favoriteItems.toList()
            favorites = ArrayList<Favorite>(favorites)
        } else return null
        return favorites as ArrayList<Favorite?>?
    }

}