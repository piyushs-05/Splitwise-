package com.example.splitwise_final.presentation.scanreceipt

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.splitwise_final.presentation.groupdetails.GroupDetailsViewModel

@Composable
fun ScanReceiptScreenWrapper(
    groupId: String,
    onBackClick: () -> Unit,
    onReceiptScanned: (String) -> Unit,
    groupViewModel: GroupDetailsViewModel = hiltViewModel()
) {
    val groupState by groupViewModel.state.collectAsState()

    LaunchedEffect(groupId) {
        groupViewModel.loadGroupDetails(groupId)
    }

    when {
        groupState.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF4CB5AE))
            }
        }
        groupState.error.isNotEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Error loading group: ${groupState.error}",
                    color = Color.Red
                )
            }
        }
        groupState.group != null -> {
            ScanReceiptScreen(
                groupId = groupId,
                members = groupState.group!!.members,
                onBackClick = onBackClick,
                onReceiptScanned = onReceiptScanned
            )
        }
    }
}

