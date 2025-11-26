package com.example.splitwise_final.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateExpenseRequest(
    @Json(name = "description")
    val description: String,
    @Json(name = "amount")
    val amount: Double,
    @Json(name = "paid_by_user_id")
    val paidByUserId: String,
    @Json(name = "split_among_user_ids")
    val splitAmongUserIds: List<String>,
    @Json(name = "group_id")
    val groupId: String,
    @Json(name = "category")
    val category: String? = null
)

@JsonClass(generateAdapter = true)
data class ExpenseDto(
    @Json(name = "id")
    val id: String,
    @Json(name = "description")
    val description: String,
    @Json(name = "amount")
    val amount: Double,
    @Json(name = "paid_by_user_id")
    val paidByUserId: String,
    @Json(name = "split_among_user_ids")
    val splitAmongUserIds: List<String>,
    @Json(name = "group_id")
    val groupId: String,
    @Json(name = "category")
    val category: String,
    @Json(name = "created_at")
    val createdAt: String
)

