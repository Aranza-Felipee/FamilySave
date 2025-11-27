package com.example.parcial2.repository

import com.example.parcial2.core.ApiService
import com.example.parcial2.core.RetrofitHelper
import com.example.parcial2.model.AddMemberRequest
import com.example.parcial2.model.CreatePlanRequest
import com.example.parcial2.model.Member
import com.example.parcial2.model.PaymentRequest
import com.example.parcial2.model.Plan
import retrofit2.Response

open class SavingRepository {

    private val api: ApiService = RetrofitHelper.getRetrofit().create(ApiService::class.java)

    open suspend fun getPlans() = api.getPlans()
    open suspend fun getPlanById(id: String) = api.getPlanById(id)
    open suspend fun createPlan(request: CreatePlanRequest): Response<Plan> {
        return api.createPlan(request)
    }


    open suspend fun getMembersByPlan(planId: String) = api.getMembersByPlan(planId)
    open suspend fun createMember(member: AddMemberRequest) = api.createMember(member)

    open suspend fun createPayment(payment: PaymentRequest) = api.createPayment(payment)
    open suspend fun getPaymentsByPlan(planId: String) = api.getPaymentsByPlan(planId)

}
