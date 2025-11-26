package com.example.splitwise_final.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CategoriesDto(
    @Json(name = "categories")
    val categories: List<String>,
    @Json(name = "examples")
    val examples: Map<String, List<String>>,
    @Json(name = "ai_powered")
    val aiPowered: Boolean
)

