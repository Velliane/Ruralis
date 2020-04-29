package com.menard.ruralis

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.menard.ruralis.data.FirestoreDataRepository
import com.menard.ruralis.data.GoogleApiRepository
import com.menard.ruralis.details.comments.Comments
import com.menard.ruralis.details.comments.CommentsViewModel
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
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

@ExperimentalCoroutinesApi
class CommentsViewModelTest {

    private lateinit var viewModel: CommentsViewModel
    @Mock
    private lateinit var mockFirestoreDataRepository: FirestoreDataRepository
    @Mock
    private lateinit var mockGoogleApiRepository: GoogleApiRepository

    private val testCoroutinesDispatcher = TestCoroutineDispatcher()
    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() = runBlockingTest {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testCoroutinesDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testCoroutinesDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun getCommentsIfPlaceFromFirestore(){
        val comments = arrayListOf(Comments("Martin", "Du super produits", "001"))
        mockFirestoreDataRepository = mock {
            onBlocking { getCommentsOfPlace("001") } doReturn comments
        }
        viewModel = CommentsViewModel(mockFirestoreDataRepository, mockGoogleApiRepository)
        viewModel.getCommentsOfPlace("001", true, "***", "###")
        val listFound = viewModel.allCommentsLiveData.getOrAwaitValue()

        assertEquals(1, listFound.size)
        assertEquals("Martin", listFound[0].name)
    }


    @Test
    fun getCommentsIfPlaceFromGoogle(){
        val comments = arrayListOf(Comments("Martin", "Du super produits", "001"))
        mockGoogleApiRepository = mock {
            onBlocking { getComments("001", "***", "###") } doReturn comments
        }
        viewModel = CommentsViewModel(mockFirestoreDataRepository, mockGoogleApiRepository)
        viewModel.getCommentsOfPlace("001", false, "***", "###")
        val listFound = viewModel.allCommentsLiveData.getOrAwaitValue()

        assertEquals(1, listFound.size)
        assertEquals("Martin", listFound[0].name)
    }

}