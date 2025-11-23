package com.example.parcial2.model

data class Plan(
    val _id: String,
    val name: String,
    val motive: String?,
    val targetAmount: Double,
    val months: Int,
    val createdAt: String


)
