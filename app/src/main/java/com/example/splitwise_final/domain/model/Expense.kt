package com.example.splitwise_final.domain.model

data class Expense(
    val id: String,
    val description: String,
    val amount: Double,
    val paidByUserId: String,
    val splitAmongUserIds: List<String>,
    val groupId: String,
    val category: String,
    val createdAt: String
)

