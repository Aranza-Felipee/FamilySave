package com.example.parcial2.core

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
    // Conexiones con los endpoints de la api

    @POST("plans")
    suspend fun createPlan(@Body plan: Plan): Response<Plan>

    @GET("plans")
    suspend fun getPlans(): Response<List<Plan>>

    @GET("plans/{id}")
    suspend fun getPlanById(@Path("id") id: String): Response<Plan>


    @POST("members")
    suspend fun createMember(@Body member: Member): Response<Member>

    @GET("members")
    suspend fun getMembers(): Response<List<Member>>

    @GET("members/plan/{planId}")
    suspend fun getMembersByPlan(@Path("planId") planId: String): Response<List<Member>>



    @POST("payments")
    suspend fun createPayment(@Body payment: PaymentRequest): Response<Payment>

    @GET("payments/plan/{planId}")
    suspend fun getPaymentsByPlan(@Path("planId") planId: String): Response<List<Payment>>
}