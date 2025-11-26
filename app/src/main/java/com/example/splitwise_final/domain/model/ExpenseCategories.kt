package com.example.splitwise_final.domain.model

data class ExpenseCategories(
    val categories: List<String>,
    val examples: Map<String, List<String>>,
    val aiPowered: Boolean
)

