package com.example.parcial2.model

import com.google.gson.annotations.SerializedName

data class Payment(
    @SerializedName("_id") val id: String,
    val amount: Double,
    val date: String,
    val planId: String,
    @SerializedName("memberId") val member: MemberRef
)

data class MemberRef(
    @SerializedName("_id") val id: String,
    val name: String? = "Desconocido"
)
