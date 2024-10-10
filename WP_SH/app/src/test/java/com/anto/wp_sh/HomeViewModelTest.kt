package com.anto.antostephen_20241003_nyc_school
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.anto.antostephen_20241003_nyc_school.data.model.SchoolDetails
import com.anto.antostephen_20241003_nyc_school.data.model.SchoolScores
import com.anto.antostephen_20241003_nyc_school.data.repo.SchoolRepository
import com.anto.antostephen_20241003_nyc_school.viewmodel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import retrofit2.Response

class HomeViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: HomeViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var mockRepository: SchoolRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = HomeViewModel(mockRepository)
    }

    @Test
    fun `test getSchoolDetails success`() = runBlocking {
        // Arrange
        val schoolDetails = SchoolDetails(/* initialize with valid data */)
        val response = mockDetailsResponse(success = true, body = schoolDetails)

        `when`(mockRepository.fetchData()).thenReturn(response)

        var observedData: SchoolDetails? = null
        var observedError: String? = null

        // Create observers to capture emitted values
        val observerData = Observer<SchoolDetails> { observedData = it }
        val observerError = Observer<String> { observedError = it }

        viewModel.data.observeForever(observerData)
        viewModel.error.observeForever(observerError)

        // Act
        viewModel.getSchoolDetails()

        // Advance the dispatcher to process the coroutine
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(schoolDetails, observedData)
        assertNull(observedError)

        // Clean up
        viewModel.data.removeObserver(observerData)
        viewModel.error.removeObserver(observerError)
    }

    @Test
    fun `test getSchoolDetails error`() = runBlocking {
        // Arrange
        val response = mockDetailsResponse(success = false, code = 404)

        `when`(mockRepository.fetchData()).thenReturn(response)

        var observedData: SchoolDetails? = null
        var observedError: String? = null

        // Create observers to capture emitted values
        val observerData = Observer<SchoolDetails> { observedData = it }
        val observerError = Observer<String> { observedError = it }

        viewModel.data.observeForever(observerData)
        viewModel.error.observeForever(observerError)

        // Act
        viewModel.getSchoolDetails()

        // Advance the dispatcher to process the coroutine
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertNull(observedData)
        assertEquals("Error: 404", observedError)

        // Clean up
        viewModel.data.removeObserver(observerData)
        viewModel.error.removeObserver(observerError)
    }

    private fun mockDetailsResponse(success: Boolean, body: SchoolDetails? = null, code: Int = 200): Response<SchoolDetails> {
        return if (success) {
            Response.success(body)
        } else {
            val errorBody = ("{\"msg\":\"There was an error\"," +
                    "\"msgType\":\"API Error\"," +
                    "\"msgBody\":\"An exception occurred while trying to fetch the School Details data from the server\"}").toResponseBody("application/json".toMediaType())
            Response.error(code, errorBody)
        }
    }


    @Test
    fun `test getSchoolScores success`() = runBlocking {
        // Arrange
        val schoolDetails = SchoolScores(/* initialize with valid data */)
        val response = mockScoresResponse(success = true, body = schoolDetails)

        `when`(mockRepository.fetchScores()).thenReturn(response)

        var observedData: SchoolScores? = null
        var observedError: String? = null

        // Create observers to capture emitted values
        val observerData = Observer<SchoolScores> { observedData = it }
        val observerError = Observer<String> { observedError = it }

        viewModel.scores.observeForever(observerData)
        viewModel.error.observeForever(observerError)

        // Act
        viewModel.getSchoolScores()

        // Advance the dispatcher to process the coroutine
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(schoolDetails, observedData)
        assertNull(observedError)

        // Clean up
        viewModel.scores.removeObserver(observerData)
        viewModel.error.removeObserver(observerError)
    }

    @Test
    fun `test getSchoolScores error`() = runBlocking {
        // Arrange
        val response = mockScoresResponse(success = false, code = 404)

        `when`(mockRepository.fetchScores()).thenReturn(response)

        var observedData: SchoolScores? = null
        var observedError: String? = null

        // Create observers to capture emitted values
        val observerData = Observer<SchoolScores> { observedData = it }
        val observerError = Observer<String> { observedError = it }

        viewModel.scores.observeForever(observerData)
        viewModel.error.observeForever(observerError)

        // Act
        viewModel.getSchoolScores()

        // Advance the dispatcher to process the coroutine
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertNull(observedData)
        assertEquals("Error: 404", observedError)

        // Clean up
        viewModel.scores.removeObserver(observerData)
        viewModel.error.removeObserver(observerError)
    }

    private fun mockScoresResponse(success: Boolean, body: SchoolScores? = null, code: Int = 200): Response<SchoolScores> {
        return if (success) {
            Response.success(body)
        } else {
            val errorBody = ("{\"msg\":\"There was an error\"," +
                    "\"msgType\":\"API Error\"," +
                    "\"msgBody\":\"An exception occurred while trying to fetch the School Scores data from the server\"}").toResponseBody("application/json".toMediaType())
            Response.error(code, errorBody)
        }
    }


}
