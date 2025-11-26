package com.example.splitwise_final.domain.model

data class GroupExpenses(
    val expenses: List<Expense>,
    val categoryBreakdown: Map<String, Double>,
    val totalAmount: Double
)

