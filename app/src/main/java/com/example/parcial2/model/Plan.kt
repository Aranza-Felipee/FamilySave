package com.example.parcial2.model

import com.google.gson.annotations.SerializedName

data class Plan(
    @SerializedName("_id")
    val id: String? = null,
    val name: String? = null,
    @SerializedName("motive")
    val category: String? = null,
    @SerializedName("targetAmount")
    val goalAmount: Double? = null,
    @SerializedName("months")
    val durationInMonths: Int? = null,
    val createdAt: String? = null,
    val members: List<Member>? = emptyList()
)