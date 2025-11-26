package com.example.parcial2.model

data class AddMemberRequest(
    val name: String,
    val planId: String,
    val contributionPerMonth: Int
)
