package com.example.parcial2.model

import com.google.gson.annotations.SerializedName

data class Member(
    @SerializedName("_id")
    val id: String? = null,
    val name: String? = null,
    val planId: String? = null,
    val contributionPerMonth: Double? = null,
    val joinedAt: String? = null
)