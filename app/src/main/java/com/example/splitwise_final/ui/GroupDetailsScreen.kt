package com.example.splitwise_final.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.splitwise_final.presentation.groupdetails.GroupDetailsScreen as PresentationGroupDetailsScreen
import com.example.splitwise_final.presentation.groupdetails.GroupDetailsViewModel

// This is a wrapper that delegates to the presentation layer GroupDetailsScreen
@Composable
fun GroupDetailsScreen(
    groupName: String = "Trip to Goa",
    groupId: String = "group_1",
    members: List<String> = listOf("Anchit", "Rahul", "Priya"),
    onBackClick: () -> Unit = {},
    onAddExpenseClick: () -> Unit = {},
    onViewBalanceClick: () -> Unit = {},
    viewModel: GroupDetailsViewModel = hiltViewModel()
) {
    // Use the presentation layer screen with backend integration
    PresentationGroupDetailsScreen(
        groupId = groupId,
        onBackClick = onBackClick,
        onViewExpenses = { _ ->
            onViewBalanceClick()
        },
        onCalculateSettlement = { _ ->
            onViewBalanceClick()
        },
        viewModel = viewModel
    )
}

