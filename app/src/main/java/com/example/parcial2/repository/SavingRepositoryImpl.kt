package com.example.parcial2.repository

import com.example.parcial2.core.ApiService
import com.example.parcial2.core.RetrofitHelper
import com.example.parcial2.model.AddMemberRequest
import com.example.parcial2.model.CreatePlanRequest
import com.example.parcial2.model.Member
import com.example.parcial2.model.Payment
import com.example.parcial2.model.PaymentRequest
import com.example.parcial2.model.Plan
import retrofit2.Response

class SavingRepositoryImpl : SavingRepository() {

    private val api: ApiService = RetrofitHelper.getRetrofit().create(ApiService::class.java)

    override suspend fun getPlans(): Response<List<Plan>> = api.getPlans()

    override suspend fun getPlanById(id: String): Response<Plan> = api.getPlanById(id)

    override suspend fun createPlan(request: CreatePlanRequest): Response<Plan> = api.createPlan(request)

    override suspend fun getMembersByPlan(planId: String): Response<List<Member>> = api.getMembersByPlan(planId)

    override suspend fun createMember(member: AddMemberRequest): Response<Member> = api.createMember(member)

    override suspend fun createPayment(payment: PaymentRequest): Response<Unit> = api.createPayment(payment)

    override suspend fun getPaymentsByPlan(planId: String): Response<List<Payment>> = api.getPaymentsByPlan(planId)
}
