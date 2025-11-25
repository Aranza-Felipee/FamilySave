package com.example.parcial2.core

import com.example.parcial2.model.CreatePlanRequest
import com.example.parcial2.model.Member
import com.example.parcial2.model.Payment
import com.example.parcial2.model.PaymentRequest
import com.example.parcial2.model.Plan
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    // PLANES
    @POST("api/plans")
    suspend fun createPlan(@Body request: CreatePlanRequest): Response<Plan>

    @GET("api/plans")
    suspend fun getPlans(): Response<List<Plan>>

    @GET("api/plans/{id}")
    suspend fun getPlanById(@Path("id") id: String): Response<Plan>


    // MIEMBROS
    @POST("api/members")
    suspend fun createMember(@Body member: Member): Response<Member>

    @GET("plans/{planId}/members")
    suspend fun getMembersByPlanId(@Path("planId") planId: String): Response<List<Member>>

    @GET("api/members/plan/{planId}")
    suspend fun getMembersByPlan(@Path("planId") planId: String): Response<List<Member>>


    // PAGOS
    @POST("api/payments")
    suspend fun createPayment(@Body payment: PaymentRequest): Response<Payment>

    @GET("api/payments/plan/{planId}")
    suspend fun getPaymentsByPlan(@Path("planId") planId: String): Response<List<Payment>>
}
