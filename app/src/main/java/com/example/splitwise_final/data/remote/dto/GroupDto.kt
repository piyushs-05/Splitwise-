package com.example.splitwise_final.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDto(
    @Json(name = "id")
    val id: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "email")
    val email: String
)

@JsonClass(generateAdapter = true)
data class CreateGroupRequest(
    @Json(name = "name")
    val name: String,
    @Json(name = "members")
    val members: List<UserDto>
)

@JsonClass(generateAdapter = true)
data class GroupDto(
    @Json(name = "id")
    val id: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "members")
    val members: List<UserDto>,
    @Json(name = "created_at")
    val createdAt: String,
    @Json(name = "total_expenses")
    val totalExpenses: Int,
    @Json(name = "total_amount")
    val totalAmount: Double
)

