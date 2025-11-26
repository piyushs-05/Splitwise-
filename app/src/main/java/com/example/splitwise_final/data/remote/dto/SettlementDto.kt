package com.example.splitwise_final.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SettlementDto(
    @Json(name = "from")
    val from: String,
    @Json(name = "to")
    val to: String,
    @Json(name = "amount")
    val amount: Double,
    @Json(name = "from_user")
    val fromUser: Map<String, String>?,
    @Json(name = "to_user")
    val toUser: Map<String, String>?,
    @Json(name = "message")
    val message: String?
)

