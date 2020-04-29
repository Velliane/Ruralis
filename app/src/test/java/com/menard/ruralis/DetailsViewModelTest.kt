package com.menard.ruralis

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.menard.ruralis.add_places.PlaceDetailed
import com.menard.ruralis.data.FavoritesDataRepository
import com.menard.ruralis.data.FirestoreDataRepository
import com.menard.ruralis.data.GoogleApiRepository
import com.menard.ruralis.details.DetailsViewModel
import com.menard.ruralis.details.Favorite
import com.menard.ruralis.utils.getOrAwaitValue
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import junit.framework.Assert.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule


@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class DetailsViewModelTest {

    private lateinit var viewModel: DetailsViewModel

    @Mock
    private lateinit var context: Context
    @Mock
    private lateinit var favoritesDataRepository: FavoritesDataRepository
    @Mock
    private lateinit var googleApiRepository: GoogleApiRepository
    @Mock
    private lateinit var firestoreDataRepository: FirestoreDataRepository

    private val testCoroutineDispatcher = TestCoroutineDispatcher()
    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() = runBlockingTest {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testCoroutineDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun checkIfExistInFavorites_ReturnFalse(){
        val mockFavoritesDataRepository = mock<FavoritesDataRepository> {
            onBlocking { getFavoriteById("001") } doReturn null
        }
        viewModel = DetailsViewModel(context, mockFavoritesDataRepository, googleApiRepository, firestoreDataRepository)

        val result = viewModel.checkIfAlreadyInFavorites("001").getOrAwaitValue()
        assertFalse(result)
    }

    @Test
    fun checkIfExistInFavorites_ReturnTrue(){
        val favorite = Favorite("002", "La ruche", null, true)
        val mockFavoritesDataRepository = mock<FavoritesDataRepository> {
            onBlocking { getFavoriteById("002") } doReturn favorite
        }
        viewModel = DetailsViewModel(context, mockFavoritesDataRepository, googleApiRepository, firestoreDataRepository)

        val result = viewModel.checkIfAlreadyInFavorites("002").getOrAwaitValue()
        assertTrue(result)
    }

    @Test
    fun getPlaceFromId_WhereFromRuralisIsTrue() {
        val place = PlaceDetailed("001", "bio", "Fruits et légumes", "La ferme", "6 impasse des Sables", arrayListOf("gr4552/image.fr"), "Du lundi au vendredi : 9h-18h", "maraicher.com", "0665364510", null, null, true)
        val mockFirestoreDataRepository = mock<FirestoreDataRepository> {
            onBlocking { getPlaceFromFirestoreById("001") } doReturn place
        }
        viewModel = DetailsViewModel(context, favoritesDataRepository, googleApiRepository, mockFirestoreDataRepository)

        val placeFound = viewModel.getPlaceAccordingItsOrigin(true, "001", "name, address, website...", "*****").getOrAwaitValue()
        assertEquals("La ferme", placeFound.name)
        assertEquals("6 impasse des Sables", placeFound.address)
    }

    @Test
    fun getPlaceFromId_WhereFromRuralisIsFalse() {
        val place = PlaceDetailed("004", "Miel","Miel", "La ruche", "21 route des Lilas", arrayListOf("gr4552/image.fr"), "Du lundi au vendredi : 9h-18h", "laruche.com", "066715520", null, null, false)
        val mockGoogleApiRepository = mock<GoogleApiRepository> {
            onBlocking { getDetails("004", "name, address, website...", "*****", context) } doReturn place
        }
        viewModel = DetailsViewModel(context, favoritesDataRepository, mockGoogleApiRepository, firestoreDataRepository)

        val placeFound = viewModel.getPlaceAccordingItsOrigin(false, "004", "name, address, website...", "*****").getOrAwaitValue()
        assertEquals("Miel", placeFound.type)
        assertEquals("laruche.com", placeFound.website)
    }

    @Test
    fun testAddingPlaceToFavorites() {
        val addingSuccess = 1L
        val favorite = Favorite("002", "La ruche", null, true)
        val mockFavoritesDataRepository = mock<FavoritesDataRepository> {
            onBlocking { addFavorites(favorite) } doReturn addingSuccess
            onBlocking { getFavoriteById("002") } doReturn favorite
        }
        viewModel = DetailsViewModel(context, mockFavoritesDataRepository, googleApiRepository, firestoreDataRepository)
        viewModel.addToFavorites("002", true, null, "La ruche")

        val checkIfExist = viewModel.checkIfAlreadyInFavorites("002").getOrAwaitValue()
        assertTrue(checkIfExist)
    }

    @Test
    fun testDeletingPlaceFromFavorites() {
        val deletingSuccess = 1
        val mockFavoritesDataRepository = mock<FavoritesDataRepository> {
            onBlocking {deleteFavorite("001") } doReturn deletingSuccess
            onBlocking { getFavoriteById("001") } doReturn null
        }
        viewModel = DetailsViewModel(context, mockFavoritesDataRepository, googleApiRepository, firestoreDataRepository)
        viewModel.deleteFromFavorites("001")

        val checkIfExist = viewModel.checkIfAlreadyInFavorites("001").getOrAwaitValue()
        assertFalse(checkIfExist)
    }
}