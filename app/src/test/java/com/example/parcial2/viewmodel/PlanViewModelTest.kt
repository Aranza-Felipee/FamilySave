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
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.Response

@ExperimentalCoroutinesApi
class PlanViewModelTest {

    private val mockRepository: SavingRepository = mock()
    private lateinit var viewModel: PlanViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = PlanViewModel(mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchPlanById - cuando la API responde exitosamente - el estado debe ser Success`() = runTest {
        // 1. Arrange
        val planId = "1"
        val expectedPlan = Plan(
            id = "1",
            name = "Plan de Ahorro",
            category = "Para un coche nuevo",
            goalAmount = 20000.0,
            durationInMonths = 12,
            createdAt = "2023-10-27T10:00:00Z",
            members = emptyList()
        )
        val mockResponse = Response.success(expectedPlan)

        whenever(mockRepository.getPlanById(planId)).thenReturn(mockResponse)

        // 2. Act
        viewModel.fetchPlanById(planId)
        testDispatcher.scheduler.advanceUntilIdle()

        // 3. Assert
        val finalState = viewModel.planDetail.value
        assertEquals(UiState.Success(expectedPlan), finalState)
    }

    @Test
    fun `fetchPlanById - cuando la API responde con error - el estado debe ser Error`() = runTest {
        // 1. Arrange
        val planId = "1"
        val errorCode = 404
        val mockResponse = Response.error<Plan>(errorCode, "".toResponseBody(null))

        whenever(mockRepository.getPlanById(planId)).thenReturn(mockResponse)

        // 2. Act
        viewModel.fetchPlanById(planId)
        testDispatcher.scheduler.advanceUntilIdle()

        // 3. Assert
        val finalState = viewModel.planDetail.value
        assertEquals(UiState.Error("Error: $errorCode o plan no encontrado"), finalState)
    }
}
