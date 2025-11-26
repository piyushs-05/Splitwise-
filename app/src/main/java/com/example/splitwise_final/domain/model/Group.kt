package com.example.splitwise_final.domain.model

data class Group(
    val id: String,
    val name: String,
    val members: List<User>,
    val createdAt: String,
    val totalExpenses: Int = 0,
    val totalAmount: Double = 0.0
)

