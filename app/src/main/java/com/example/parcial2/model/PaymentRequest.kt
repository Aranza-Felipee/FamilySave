package com.example.parcial2.model

data class PaymentRequest(
    val planId: String,
    val memberId: String,
    val amount: Double,
    val date: String
)