package com.example.parcial2.repository

import com.example.parcial2.core.ApiService
import com.example.parcial2.core.RetrofitHelper
import com.example.parcial2.model.AddMemberRequest
import com.example.parcial2.model.CreatePlanRequest
import com.example.parcial2.model.Member
import com.example.parcial2.model.PaymentRequest
import com.example.parcial2.model.Plan
import retrofit2.Response

class SavingRepository {

    private val api: ApiService = RetrofitHelper.getRetrofit().create(ApiService::class.java)

    suspend fun getPlans() = api.getPlans()
    suspend fun getPlanById(id: String) = api.getPlanById(id)
    suspend fun createPlan(request: CreatePlanRequest): Response<Plan> {
        return api.createPlan(request)
    }


    suspend fun getMembersByPlan(planId: String) = api.getMembersByPlan(planId)
    suspend fun createMember(member: AddMemberRequest) = api.createMember(member)

    suspend fun createPayment(payment: PaymentRequest) = api.createPayment(payment)
    suspend fun getPaymentsByPlan(planId: String) = api.getPaymentsByPlan(planId)

}
