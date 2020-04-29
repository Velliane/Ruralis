package com.menard.ruralis

import android.content.Context
import android.location.Location
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.menard.ruralis.data.ConnectivityRepository
import com.menard.ruralis.data.FirestoreDataRepository
import com.menard.ruralis.data.FusedLocationRepository
import com.menard.ruralis.data.GoogleApiRepository
import com.menard.ruralis.search_places.PlaceForList
import com.menard.ruralis.search_places.list.ListViewModel
import com.menard.ruralis.search_places.map.MapViewModel
import com.menard.ruralis.utils.getOrAwaitValue
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import junit.framework.Assert
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
class MapViewModelTest {

    private lateinit var viewModel: MapViewModel

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var googleApiRepository: GoogleApiRepository

    @Mock
    private lateinit var firestoreDataRepository: FirestoreDataRepository
    @Mock
    private lateinit var mockConnectivityRepository: ConnectivityRepository
    @Mock
    private lateinit var fusedLocationRepository: FusedLocationRepository

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
    fun getAllPlaces() = runBlockingTest{
        val mockConnectionLiveData = MutableLiveData<Boolean>()
        mockConnectionLiveData.value = true
        mockConnectivityRepository = mock {
            onBlocking { connectivityLiveData } doReturn mockConnectionLiveData
        }
        val listFromGoogle = arrayListOf(
            PlaceForList("01", "La ruche", "HONEY", "img.fr", "46.23564", "5.32145", "4 rue des lilas", false, false),
            PlaceForList("02", "La ferme de ma GM", "DAIRY", "65/image.fr", "46.35467", "5.65448", "25 chemin des pâquerettes", false, true)
        )
        val mockGoogleApiRepository = mock<GoogleApiRepository> {
            onBlocking {
                getTextSearch(context, "46.66554,5.65446", "6000", "XXXX", "XX") } doReturn listFromGoogle
        }
        val list = arrayListOf(
            PlaceForList("05", "EARL des Platanes", "MEAT", "694/img.fr", "46.23456", "5.32178", "2 chemin des pâquerettes", true, true),
            PlaceForList("04", "La ferme", "EGG", "646/image.fr", "46.23567", "5.654445","25 chemin des pâquerettes",  true, false)
        )
        val location = Location("")
        location.latitude = 46.66554
        location.longitude = 5.65446
        val mockFirestoreDataRepository = mock<FirestoreDataRepository>{
            onBlocking { getAllPlacesFromFirestore(location, "6000", context) } doReturn list
        }
        viewModel = MapViewModel(fusedLocationRepository, mockConnectivityRepository, context, mockGoogleApiRepository, mockFirestoreDataRepository)

        viewModel.getAllPlaces("46.66554,5.65446", "6000", "producteur", "XXXX", location)
        val listPlaceFound = viewModel.allPlaceLiveData.getOrAwaitValue()

        Assert.assertTrue(listPlaceFound.isNotEmpty())
        Assert.assertEquals("MEAT", listPlaceFound[0].type)
        Assert.assertEquals("694/img.fr", listPlaceFound[0].photos)
    }
}