package com.menard.ruralis

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.firebase.ui.auth.AuthUI
import com.menard.ruralis.search_places.MainViewModel
import com.menard.ruralis.utils.getOrAwaitValue
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
class MainViewModelTest {

    private lateinit var viewModel: MainViewModel

    @Mock
    private lateinit var context: Context
    @Mock
    private lateinit var authUI: AuthUI

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
    fun getUser(){
        viewModel = MainViewModel(context, authUI)
        val user = viewModel.updateHeader("Manon", "img.fr", "manon@free.fr").getOrAwaitValue()

        Assert.assertEquals("Manon", user.name)
        Assert.assertEquals("img.fr", user.photo)
        Assert.assertEquals("manon@free.fr", user.email)
    }
}