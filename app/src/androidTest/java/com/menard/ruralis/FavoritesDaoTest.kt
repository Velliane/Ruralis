package com.menard.ruralis

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.platform.app.InstrumentationRegistry
import com.menard.ruralis.database.FavoritesDatabase
import com.menard.ruralis.details.Favorite
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.lang.Exception


@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class FavoritesDaoTest {

    private lateinit var favoritesDatabase: FavoritesDatabase

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun initDb(){
        favoritesDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().context, FavoritesDatabase::class.java)
            .allowMainThreadQueries().build()
    }

    @After
    @Throws(Exception::class)
    fun closeDb(){
        favoritesDatabase.close()
    }

    @Test
    @Throws(InterruptedException::class)
    fun insertAPlaceInFavorite_AndGetItById()= runBlockingTest{
        val favorite = Favorite("001", "La ferme de GM", "54sdf/img.fr", false)
        favoritesDatabase.favoritesDao().addFavorite(favorite)

        val favoriteFound = favoritesDatabase.favoritesDao().getFavoriteById("001")
        assertEquals("001", favoriteFound?.id)
        assertEquals("La ferme de GM", favoriteFound?.name)
    }

    @Test
    @Throws(InterruptedException::class)
    fun getAllFavorites_DatabaseNotEmpty_ContainsOne()= runBlockingTest{
        val favorite = Favorite("001", "La ferme de GM", "54sdf/img.fr", false)
        favoritesDatabase.favoritesDao().addFavorite(favorite)

        val favorites = favoritesDatabase.favoritesDao().getAllFavorites()

        assertTrue(favorites.isNotEmpty())
        assertTrue(favorites.size == 1)
    }

    @Test
    @Throws(InterruptedException::class)
    fun getAllFavorites_DatabaseNotEmpty_ContainsTwo()= runBlockingTest{
        val favorite = Favorite("001", "La ferme de GM", "54sdf/img.fr", false)
        favoritesDatabase.favoritesDao().addFavorite(favorite)
        val favorite2 = Favorite("002", "La ruche", "65xx/img.fr", true)
        favoritesDatabase.favoritesDao().addFavorite(favorite2)

        val favorites = favoritesDatabase.favoritesDao().getAllFavorites()

        assertTrue(favorites.isNotEmpty())
        assertTrue(favorites.size == 2)
        assertEquals("65xx/img.fr", favorites[1].photo)
        assertEquals(false, favorites[0].fromRuralis)
    }

    @Test
    @Throws(InterruptedException::class)
    fun getAllFavorites_DatabaseEmpty()= runBlockingTest {
        val favorites = favoritesDatabase.favoritesDao().getAllFavorites()
        assertTrue(favorites.isEmpty())
    }

    @Test
    @Throws(InterruptedException::class)
    fun addFavoriteAndDeleteIt()= runBlockingTest{
        val favorite = Favorite("001", "La ferme de GM", "54sdf/img.fr", false)
        favoritesDatabase.favoritesDao().addFavorite(favorite)
        favoritesDatabase.favoritesDao().deleteFavorite("001")

        val favorites = favoritesDatabase.favoritesDao().getAllFavorites()
        assertTrue(favorites.isEmpty())
    }
}