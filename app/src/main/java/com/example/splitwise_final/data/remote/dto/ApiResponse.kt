package com.example.splitwise_final.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiResponse(
    @Json(name = "success")
    val success: Boolean,
    @Json(name = "message")
    val message: String,
    @Json(name = "data")
    val data: Map<String, Any?>? = null
)

