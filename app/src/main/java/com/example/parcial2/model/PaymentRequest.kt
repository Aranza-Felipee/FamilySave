package com.example.parcial2.model

import com.google.gson.annotations.SerializedName

data class PaymentRequest(
    @SerializedName("planId")
    val planId: String,
    @SerializedName("memberId")
    val memberId: String,
    @SerializedName("amount")
    val amount: Double
)
