package com.menard.ruralis

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.menard.ruralis.data.ConnectivityRepository
import com.menard.ruralis.data.FirestoreDataRepository
import com.menard.ruralis.quiz.Question
import com.menard.ruralis.quiz.QuizViewModel
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
class QuizViewModelTest {

    private lateinit var viewModel: QuizViewModel

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
    fun getQuestionListWhenNotConnected(){
        val mockConnectionLiveData = MutableLiveData<Boolean>()
        mockConnectionLiveData.value = false
        val connectivityRepository = mock<ConnectivityRepository> {
            onBlocking { connectivityLiveData } doReturn mockConnectionLiveData
        }
        val listOfId = arrayListOf(2, 3)
        val list = arrayListOf(Question("Quel est la durée de gestion d'une vache", "3 mois",
            "6 mois", "9 mois", 3,
            2, "La durée de gestation est de 9 mois."),
        Question("Comment appelle-t-on le petit de la chèvre ?", "Le chevrou", "Le chrevreau",
            "Le chevron", 2, 3, "Le chevreau/ La chevrette")
        )
        val mockFirestoreDataRepository = mock<FirestoreDataRepository> {
            onBlocking { getListOfQuestion(listOfId) } doReturn list
        }
        viewModel = QuizViewModel(mockFirestoreDataRepository, connectivityRepository)
        viewModel.getListOfQuestion(listOfId)

        assertEquals(null, viewModel._quizLiveData.value)
    }

    @Test
    fun getListOfQuestionWhenConnected(){
        val mockConnectionLiveData = MutableLiveData<Boolean>()
        mockConnectionLiveData.value = true
        val connectivityRepository = mock<ConnectivityRepository> {
            onBlocking { connectivityLiveData } doReturn mockConnectionLiveData
        }
        val listOfId = arrayListOf(2, 3)
        val list = arrayListOf(Question("Quel est la durée de gestion d'une vache", "3 mois",
            "6 mois", "9 mois", 3,
            2, "La durée de gestation est de 9 mois."),
            Question("Comment appelle-t-on le petit de la chèvre ?", "Le chevrou", "Le chrevreau",
                "Le chevron", 2, 3, "Le chevreau/ La chevrette")
        )
        val mockFirestoreDataRepository = mock<FirestoreDataRepository> {
            onBlocking { getListOfQuestion(listOfId) } doReturn list
        }
        viewModel = QuizViewModel(mockFirestoreDataRepository, connectivityRepository)
        viewModel.getListOfQuestion(listOfId)
        val result = viewModel._quizLiveData.getOrAwaitValue()

        assertEquals(2, result.size)
    }

}