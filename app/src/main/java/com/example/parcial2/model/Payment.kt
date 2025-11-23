package com.example.parcial2.model

data class Payment(
    val _id: String,
    val memberId: String,
    val planId: String,
    val amount: Double,
    val date: String
)