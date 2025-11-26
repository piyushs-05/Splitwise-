package com.example.splitwise_final.domain.model

data class ReceiptScanResult(
    val expense: Expense,
    val scannedAmount: Double,
    val vendor: String,
    val category: String
)

