package com.example.splitwise_final.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.splitwise_final.presentation.settlement.SettlementScreen
import com.example.splitwise_final.presentation.settlement.SettlementViewModel

// This is a wrapper that delegates to the presentation layer SettlementScreen
@Composable
fun BalanceScreen(
    groupName: String = "Trip to Goa",
    groupId: String = "group_1",
    onBackClick: () -> Unit = {},
    viewModel: SettlementViewModel = hiltViewModel()
) {
    // Use the presentation layer settlement screen with backend integration
    SettlementScreen(
        groupId = groupId,
        onBackClick = onBackClick,
        viewModel = viewModel
    )
}

