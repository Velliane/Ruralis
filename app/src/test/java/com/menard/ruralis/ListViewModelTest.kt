package com.menard.ruralis

import android.content.Context
import android.location.Location
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.menard.ruralis.data.FirestoreDataRepository
import com.menard.ruralis.data.GoogleApiRepository
import com.menard.ruralis.search_places.PlaceForList
import com.menard.ruralis.search_places.list.ListViewModel
import com.menard.ruralis.utils.getOrAwaitValue
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
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
class ListViewModelTest {

    private lateinit var viewModel: ListViewModel

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var googleApiRepository: GoogleApiRepository

    @Mock
    private lateinit var firestoreDataRepository: FirestoreDataRepository


    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()
    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    @Rule
    @JvmField
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
    fun getOnlyPlacesFromGoogle() = runBlockingTest {
        val list = arrayListOf(
            PlaceForList("01", "La ruche", "HONEY", "img.fr", "46.23564", "5.32145", false),
            PlaceForList("02", "La ferme de ma GM", "DAIRY", "65/image.fr", "46.35467", "5.65448", false)
        )
        val mockGoogleApiRepository = mock<GoogleApiRepository> {
            onBlocking {
                getTextSearch(context, "46.66554,5.65446", "6000", "XXXX", "XX") } doReturn list
        }
        viewModel = ListViewModel(context, mockGoogleApiRepository, firestoreDataRepository)

        viewModel.getTextSearch("46.66554,5.65446", "6000", "producteur", "XXXX")
        val listPlaceFound = viewModel.placeTextSearchListLiveData.getOrAwaitValue()

        assertEquals("La ruche", listPlaceFound[0].name)
        assertEquals("02", listPlaceFound[1].placeId)
    }

    @Test
    fun getOnlyPlacesFromFirestore() = runBlockingTest{
        val list = arrayListOf(
            PlaceForList("05", "EARL des Platanes", "MEAT", "694/img.fr", "46.23456", "5.32178", true),
            PlaceForList("04", "La ferme", "EGG", "646/image.fr", "46.23567", "5.654445", true)
        )
        val location = Location("")
        location.latitude = 46.66554
        location.longitude = 5.65446
        val mockFirestoreDataRepository = mock<FirestoreDataRepository>{
            onBlocking { getAllPlacesFromFirestore(location, "6000", context) } doReturn list
        }
        viewModel = ListViewModel(context, googleApiRepository, mockFirestoreDataRepository)

        viewModel.getPlacesFromFirestoreAccordingUserLocation(location, "6000")
        val listPlaceFound = viewModel.placeListLiveData.getOrAwaitValue()

        assertTrue(listPlaceFound.isNotEmpty())
        assertEquals("MEAT", listPlaceFound[0].type)
        assertEquals("694/img.fr", listPlaceFound[0].photos)
    }
}