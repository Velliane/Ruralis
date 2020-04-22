package com.menard.ruralis

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.menard.ruralis.data.ConnectivityRepository
import com.menard.ruralis.data.FavoritesDataRepository
import com.menard.ruralis.data.KnowsItRepository
import com.menard.ruralis.details.Favorite
import com.menard.ruralis.knowsit.HomeViewModel
import com.menard.ruralis.knowsit.KnowsIt
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
class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel

    @Mock
    private lateinit var mockKnowsItDataRepository: KnowsItRepository
    @Mock
    private lateinit var mockFavoritesDataRepository: FavoritesDataRepository
    @Mock
    private lateinit var connectivityRepository: ConnectivityRepository

    private val testCoroutineDispatcher = TestCoroutineDispatcher()
    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() = runBlockingTest {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testCoroutineDispatcher)
        val knowsIt = KnowsIt(10, "Les poules pondent environ un oeuf toutes les 26h", "egg")
        mockKnowsItDataRepository = mock {
            onBlocking { getRandomKnowsIt() } doReturn knowsIt
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testCoroutineDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun getKnowsIt_WhenNotConnected(){
        val mockConnectionLiveData = MutableLiveData<Boolean>()
        mockConnectionLiveData.value = false
        connectivityRepository = mock {
            onBlocking { connectivityLiveData } doReturn mockConnectionLiveData
        }
        viewModel = HomeViewModel(connectivityRepository, mockFavoritesDataRepository, mockKnowsItDataRepository)

        viewModel.getRandomKnowsIt()
        val result = viewModel.homeLiveData.value
        assertEquals(null, result)
    }

    @Test
    fun getKnowsIt_WhenConnected(){
        val mockConnectionLiveData = MutableLiveData<Boolean>()
        mockConnectionLiveData.value = true
        connectivityRepository = mock {
            onBlocking { connectivityLiveData } doReturn mockConnectionLiveData
        }
        viewModel = HomeViewModel(connectivityRepository, mockFavoritesDataRepository, mockKnowsItDataRepository)

        viewModel.getRandomKnowsIt()
        val result = viewModel.homeLiveData.getOrAwaitValue()
        assertEquals("egg", result.drawable)
        assertEquals("Les poules pondent environ un oeuf toutes les 26h", result.info)
        assertEquals(10, result.id_knows_it)
    }

    @Test
    fun getFavoritesFromDatabase_ContainsTwoItems(){
        val list = arrayListOf(Favorite("004", "La ferme de GM", null, true),
        Favorite("006", "La ruche", "image.fr", false))
        mockFavoritesDataRepository = mock {
            onBlocking { getAllFavorites() } doReturn list
        }
        viewModel = HomeViewModel(connectivityRepository, mockFavoritesDataRepository, mockKnowsItDataRepository)
        val listFound = viewModel.showAllFavorites().getOrAwaitValue()
        assertEquals(2, listFound.size)
        assertEquals("004", listFound[0].id)
        assertEquals(false, listFound[1].fromRuralis)
    }

    @Test
    fun getFavoritesFromDatabase_ContainsZeroItem(){
        mockFavoritesDataRepository = mock {
            onBlocking { getAllFavorites() } doReturn emptyList()
        }
        viewModel = HomeViewModel(connectivityRepository, mockFavoritesDataRepository, mockKnowsItDataRepository)
        val listFound = viewModel.showAllFavorites().getOrAwaitValue()
        assertTrue(listFound.isEmpty())
    }

    @Test
    fun getUser(){
        viewModel = HomeViewModel(connectivityRepository, mockFavoritesDataRepository, mockKnowsItDataRepository)
        val user = viewModel.updateHeader("Manon", "img.fr", "manon@free.fr").getOrAwaitValue()

        assertEquals("Manon", user.name)
        assertEquals("img.fr", user.photo)
        assertEquals("manon@free.fr", user.email)
    }
}