package com.example.splitwise_final.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReceiptScanDto(
    @Json(name = "total_amount")
    val totalAmount: Double,
    @Json(name = "vendor")
    val vendor: String?,
    @Json(name = "raw_text")
    val rawText: String?
)

