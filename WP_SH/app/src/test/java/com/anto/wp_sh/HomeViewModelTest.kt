package com.anto.wp_sh

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.anto.wp_sh.data.model.SchoolDetails
import com.anto.wp_sh.data.repo.SchoolRepository
import com.anto.wp_sh.viewmodel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.*
import okhttp3.ResponseBody
import org.junit.*
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule() // To run LiveData synchronously

    @get:Rule
    val mockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var repository: SchoolRepository

    private lateinit var viewModel: HomeViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = HomeViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }

    @Test
    fun `getSchoolDetails should post data when API call is successful`() = runTest {
        // Given
        val schoolDetails = SchoolDetails(/*initialize with appropriate values*/)
        `when`(repository.fetchData()).thenReturn(Response.success(schoolDetails))

        // Observer for testing LiveData
        val observer = mock(Observer::class.java) as Observer<SchoolDetails>
        viewModel.data.observeForever(observer)

        // When
        viewModel.getSchoolDetails()

        // Fast-forward to allow the coroutine to execute
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(repository).fetchData()
        verify(observer).onChanged(schoolDetails)
    }

    @Test
    fun `getSchoolDetails should post error when API call fails`() = runTest {
        // Given
        val errorResponse = Response.error<SchoolDetails>(404, mock(ResponseBody::class.java))
        `when`(repository.fetchData()).thenReturn(errorResponse)

        // Observer for testing LiveData
        val observer = mock(Observer::class.java) as Observer<String>
        viewModel.error.observeForever(observer)

        // When
        viewModel.getSchoolDetails()

        // Fast-forward to allow the coroutine to execute
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(repository).fetchData()
        verify(observer).onChanged("Error: 404")
    }

    @Test
    fun `getSchoolDetails should post exception when API call throws an exception`() = runTest {
        // Given
        `when`(repository.fetchData()).thenThrow(IOException("Network error"))

        // Observer for testing LiveData
        val observer = mock(Observer::class.java) as Observer<String>
        viewModel.error.observeForever(observer)

        // When
        viewModel.getSchoolDetails()

        // Fast-forward to allow the coroutine to execute
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(repository).fetchData()
        verify(observer).onChanged("Exception: Network error")
    }
}
