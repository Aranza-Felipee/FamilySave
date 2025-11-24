package com.example.parcial2.repository

import com.example.parcial2.core.ApiService
import com.example.parcial2.core.RetrofitHelper
import com.example.parcial2.model.Member
import com.example.parcial2.model.PaymentRequest
import com.example.parcial2.model.Plan

class SavingRepository {

    private val api: ApiService = RetrofitHelper.getRetrofit().create(ApiService::class.java)

    suspend fun getPlans() = api.getPlans()
    suspend fun getPlanById(id: String) = api.getPlanById(id)
    suspend fun createPlan(plan: Plan) = api.createPlan(plan)

    suspend fun getMembers() = api.getMembers()
    suspend fun getMembersByPlan(planId: String) = api.getMembersByPlan(planId)
    suspend fun createMember(member: Member) = api.createMember(member)

    suspend fun createPayment(payment: PaymentRequest) = api.createPayment(payment)
    suspend fun getPaymentsByPlan(planId: String) = api.getPaymentsByPlan(planId)

}
