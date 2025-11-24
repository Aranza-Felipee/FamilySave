package com.example.parcial2.model

data class CreatePlanRequest(
    val name: String,
    val motive: String,
    val targetAmount: Double,
    val months: Int
)
