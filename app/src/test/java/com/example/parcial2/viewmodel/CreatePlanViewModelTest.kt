package com.example.parcial2.viewmodel

import com.example.parcial2.core.UiState
import com.example.parcial2.model.Plan
import com.example.parcial2.repository.SavingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.Response

@ExperimentalCoroutinesApi
class CreatePlanViewModelTest {

    private val mockRepository: SavingRepository = mock()
    private lateinit var viewModel: CreatePlanViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CreatePlanViewModel(mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `createPlan - cuando la API responde exitosamente - el estado debe ser Success`() = runTest {
        // Arrange
        val planName = "Viaje"
        val motive = "Vacaciones"
        val amount = 2000.0
        val months = 12

        val expectedPlan = Plan(
            id = "plan123",
            name = planName,
            category = motive,
            goalAmount = amount,
            durationInMonths = months,
            createdAt = "2023-01-01T00:00:00Z",
            members = emptyList()
        )
        val mockResponse = Response.success(expectedPlan)

        whenever(mockRepository.createPlan(any())).thenReturn(mockResponse)

        // Act
        viewModel.createPlan(planName, motive, amount, months)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val finalState = viewModel.createState.value
        assertEquals(UiState.Success(expectedPlan), finalState)
    }

    @Test
    fun `createPlan - cuando la API responde con error - el estado debe ser Error`() = runTest {
        // Arrange
        val planName = "Viaje"
        val motive = "Vacaciones"
        val amount = 2000.0
        val months = 12
        val errorCode = 500
        val mockResponse = Response.error<Plan>(errorCode, "".toResponseBody(null))

        whenever(mockRepository.createPlan(any())).thenReturn(mockResponse)

        // Act
        viewModel.createPlan(planName, motive, amount, months)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val finalState = viewModel.createState.value
        assertEquals(UiState.Error("Error: $errorCode"), finalState)
    }
}
