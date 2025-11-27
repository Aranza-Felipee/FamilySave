package com.example.parcial2.viewmodel

import com.example.parcial2.core.UiState
import com.example.parcial2.model.MemberRef
import com.example.parcial2.model.Payment
import com.example.parcial2.model.PaymentRequest
import com.example.parcial2.repository.SavingRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import retrofit2.Response

@ExperimentalCoroutinesApi
class PaymentViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: PaymentViewModel
    private val repository: SavingRepository = mockk()

    @Before
    fun setUp() {
        viewModel = PaymentViewModel(repository)
    }

    @Test
    fun `fetchPaymentsByPlan success`() = runTest {
        val planId = "plan1"
        val payments = listOf(Payment(id = "1", planId = planId, member = MemberRef(id = "member1"), amount = 100.0, date = "2024-01-01"))
        coEvery { repository.getPaymentsByPlan(planId) } returns Response.success(payments)

        viewModel.fetchPaymentsByPlan(planId)
        advanceUntilIdle()

        val state = viewModel.payments.value
        assertTrue(state is UiState.Success)
        assertEquals(payments, (state as UiState.Success).data)
    }

    @Test
    fun `fetchPaymentsByPlan error`() = runTest {
        val planId = "plan1"
        coEvery { repository.getPaymentsByPlan(planId) } returns Response.error(404, mockk(relaxed = true))

        viewModel.fetchPaymentsByPlan(planId)
        advanceUntilIdle()

        val state = viewModel.payments.value
        assertTrue(state is UiState.Error)
        assertEquals("Error: 404", (state as UiState.Error).message)
    }

    @Test
    fun `registerPayment success`() = runTest {
        val request = PaymentRequest(planId = "plan1", memberId = "member1", amount = 100.0)
        coEvery { repository.createPayment(request) } returns Response.success(Unit)

        viewModel.registerPayment(request)
        advanceUntilIdle()

        assertTrue(viewModel.paymentResult.value is UiState.Success)
    }

    @Test
    fun `registerPayment error`() = runTest {
        val request = PaymentRequest(planId = "plan1", memberId = "member1", amount = 100.0)
        coEvery { repository.createPayment(request) } returns Response.error(500, mockk(relaxed = true))

        viewModel.registerPayment(request)
        advanceUntilIdle()
        
        val state = viewModel.paymentResult.value
        assertTrue(state is UiState.Error)
        assertEquals("Error: 500", (state as UiState.Error).message)
    }

    @Test
    fun `resetState should set paymentResult to Idle`() = runTest {
        viewModel.setError("An error")
        viewModel.resetState()
        assertEquals(UiState.Idle, viewModel.paymentResult.value)
    }

    @Test
    fun `onDispose should set paymentResult to Idle`() = runTest {
        viewModel.setError("An error")
        viewModel.onDispose()
        assertEquals(UiState.Idle, viewModel.paymentResult.value)
    }
}

@ExperimentalCoroutinesApi
class MainCoroutineRule(private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()) : TestWatcher() {
    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}
