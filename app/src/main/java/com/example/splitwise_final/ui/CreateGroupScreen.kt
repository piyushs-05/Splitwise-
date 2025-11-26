package com.example.splitwise_final.ui

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.splitwise_final.presentation.creategroup.CreateGroupScreen as PresentationCreateGroupScreen
import com.example.splitwise_final.presentation.creategroup.CreateGroupViewModel

// This is a wrapper that delegates to the presentation layer CreateGroupScreen
@Composable
fun CreateGroupScreen(
    onBackClick: () -> Unit = {},
    onCreateClick: (String, List<String>) -> Unit = { _, _ -> },
    viewModel: CreateGroupViewModel = hiltViewModel()
) {
    // Use the presentation layer screen with backend integration
    PresentationCreateGroupScreen(
        onBackClick = onBackClick,
        onGroupCreated = { _ ->
            onBackClick()
        },
        viewModel = viewModel
    )
}

