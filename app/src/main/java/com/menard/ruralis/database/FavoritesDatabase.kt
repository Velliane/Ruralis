package com.menard.ruralis.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.menard.ruralis.details.Favorite
import com.menard.ruralis.quiz.HighScore

@Database(
    entities = [Favorite::class, HighScore::class],
    version = 1,
    exportSchema = false
)

abstract class FavoritesDatabase: RoomDatabase() {

    abstract fun favoritesDao(): FavoritesDao
    abstract fun highScoreDao(): HighScoreDao

    companion object {
        private var INSTANCE: FavoritesDatabase? = null

        fun getInstance(context: Context): FavoritesDatabase {
            if (INSTANCE == null) {
                synchronized(FavoritesDatabase::class) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                                context.applicationContext,
                                FavoritesDatabase::class.java,
                                "Favorites.db"
                            ).setJournalMode(JournalMode.TRUNCATE)
                            .build()
                    }
                }
            }
            return INSTANCE as FavoritesDatabase
        }

        fun destroyInstance(){
            INSTANCE = null
        }

    }
}