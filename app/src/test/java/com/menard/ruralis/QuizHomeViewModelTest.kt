package com.menard.ruralis

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.menard.ruralis.data.HighScoreRepository
import com.menard.ruralis.quiz.HighScore
import com.menard.ruralis.quiz.QuizHomeViewModel
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
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule


@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class QuizHomeViewModelTest {

    private lateinit var viewModel: QuizHomeViewModel

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
    fun testGetAllHighScoreFromDatabase()= runBlockingTest{
        val list = arrayListOf(HighScore("01", "22/04/2020", "8", "Player1"),
            HighScore("02", "21/04/2020", "7", "Player2")
        )
        val mockHighScoreRepository = mock<HighScoreRepository> {
            onBlocking { getAllHighScore() } doReturn list
        }
        viewModel = QuizHomeViewModel(mockHighScoreRepository)
        viewModel.getAllHighScore()

        val listFound = viewModel.listHighScoreLiveData.getOrAwaitValue()
        assertEquals(2, listFound.size)
        assertEquals("22/04/2020", listFound[0].date)
    }

}