package com.example.splitwise_final.domain.model

data class Settlement(
    val from: String,
    val to: String,
    val amount: Double,
    val fromUserName: String,
    val toUserName: String,
    val message: String
)

data class SettlementResult(
    val settlements: List<Settlement>,
    val balances: Map<String, Double>,
    val totalTransactions: Int
)

