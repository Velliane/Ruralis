package com.menard.ruralis

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.menard.ruralis.data.FirestoreDataRepository
import com.menard.ruralis.details.photos.Photo
import com.menard.ruralis.details.photos.PhotosViewModel
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
class PhotosViewModelTest {


    private lateinit var viewModel: PhotosViewModel

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
    fun getAllPhotosFromFirestore(){
        val list = arrayListOf(Photo("54img/fr", "la ruche", false), Photo("65img.fr", "la ruche", true))
        firestoreDataRepository = mock {
            onBlocking { getPhotosInRealTime("001") } doReturn list
        }
        viewModel = PhotosViewModel(firestoreDataRepository)
        viewModel.getAllPhotosAccordingOrigin(true, "001", emptyList())

        val listFound = viewModel.listPhotosLiveData.getOrAwaitValue()
        assertEquals(2, listFound.size)
        assertEquals("la ruche", listFound[0].name)
        assertEquals("65img.fr", listFound[1].uri)
    }
}