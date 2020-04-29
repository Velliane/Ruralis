package com.menard.ruralis

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.menard.ruralis.add_places.AddViewModel
import com.menard.ruralis.add_places.PlaceDetailed
import com.menard.ruralis.add_places.geocode_model.GeocodeRepository
import com.menard.ruralis.data.FirestoreDataRepository
import com.menard.ruralis.utils.getOrAwaitValue
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import junit.framework.Assert.assertEquals
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
class AddViewModelTest {

    private lateinit var viewModel: AddViewModel

    @Mock
    private lateinit var firestoreDataRepository: FirestoreDataRepository
    @Mock
    private lateinit var geocodeRepository: GeocodeRepository
    @Mock
    private lateinit var context: Context

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
    fun getPlaceFromFirestoreById(){
        val place = PlaceDetailed("001", "Bio","Fruits et légumes", "La ferme", "6 impasse des Sables" ,arrayListOf("gr4552/image.fr"), "Du lundi au vendredi : 9h-18h", "maraicher.com", "0665364510", null, null, true)
        val mockFirestoreDataRepository = mock<FirestoreDataRepository> {
            onBlocking { getPlaceDetailedById("001") } doReturn place
        }
        viewModel = AddViewModel(mockFirestoreDataRepository, geocodeRepository, context)

        val placeFound = viewModel.getPlaceDetailsById("001").getOrAwaitValue()
        assertEquals("La ferme", placeFound.name)
        assertEquals("Du lundi au vendredi : 9h-18h", placeFound.openingsHours)
    }

    @Test
    fun returnOpeningLiveData_WhenDayNull(){
        viewModel = AddViewModel(firestoreDataRepository, geocodeRepository, context)
        val result = viewModel.addOpeningToRecyclerView(null, "08:00", "12:00").getOrAwaitValue()
        assertEquals(null, result)
    }

    @Test
    fun returnOpeningLiveData_WhenHoursNull(){
        viewModel = AddViewModel(firestoreDataRepository, geocodeRepository, context)
        val result = viewModel.addOpeningToRecyclerView("Lundi", null, null).getOrAwaitValue()
        assertEquals(null, result)
    }

    @Test
    fun returnOpeningLiveData_WhenDayAndHoursAreSet(){
        viewModel = AddViewModel(firestoreDataRepository, geocodeRepository, context)
        val result = viewModel.addOpeningToRecyclerView("Lundi", "08:00", "12:00").getOrAwaitValue()
        assertEquals("Lundi: 08:00/12:00", result)
    }
}