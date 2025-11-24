package com.example.parcial2.model

import com.google.gson.annotations.SerializedName

data class Member(
    @SerializedName("_id")
    val id: String,
    val name: String,
    val planId: String,
    val contributionPerMonth: Double,
    val joinedAt: String
)
