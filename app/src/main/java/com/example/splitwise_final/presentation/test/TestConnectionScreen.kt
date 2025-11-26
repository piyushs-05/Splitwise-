package com.example.splitwise_final.presentation.test

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun TestConnectionScreen(
    onNavigateToCategories: () -> Unit = {},
    onNavigateToCreateGroup: () -> Unit = {},
    onNavigateToAddExpense: () -> Unit = {},
    onNavigateToExpenseList: (String) -> Unit = {},
    onNavigateToSettlement: (String) -> Unit = {},
    onNavigateToGroupDetails: (String) -> Unit = {},
    viewModel: TestViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showGroupIdDialog by remember { mutableStateOf(false) }
    var showSettlementDialog by remember { mutableStateOf(false) }
    var showGroupDetailsDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Test Hilt & Retrofit",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4CB5AE),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Status Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when {
                    state.isLoading -> {
                        CircularProgressIndicator(
                            color = Color(0xFF4CB5AE),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Testing connection...",
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }

                    state.message.isNotEmpty() -> {
                        Text(
                            text = "✓ Success",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4CB5AE),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = state.message,
                            fontSize = 16.sp,
                            color = Color.DarkGray,
                            textAlign = TextAlign.Center
                        )
                    }

                    state.error.isNotEmpty() -> {
                        Text(
                            text = "✗ Error",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Red,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = state.error,
                            fontSize = 14.sp,
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )
                    }

                    else -> {
                        Text(
                            text = "Ready to test",
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Test Button
        Button(
            onClick = { viewModel.testConnection() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CB5AE)
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = !state.isLoading
        ) {
            Text(
                text = "Test Connection",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Categories Button
        if (state.message.isNotEmpty()) {
            OutlinedButton(
                onClick = onNavigateToCategories,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF4CB5AE)
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "View Categories →",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            // Create Group Button
            OutlinedButton(
                onClick = onNavigateToCreateGroup,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF4CB5AE)
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "Create Group →",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            // View Group Details Button
            OutlinedButton(
                onClick = { showGroupDetailsDialog = true },
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF4CB5AE)
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "View Group Details →",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            // Add Expense Button
            // View Expenses Button
            if (state.message.isNotEmpty()) {
                OutlinedButton(
                    onClick = { showGroupIdDialog = true },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF4CB5AE)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(
                        text = "View Expenses →",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                // Calculate Settlement Button
                OutlinedButton(
                    onClick = { showSettlementDialog = true },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF4CB5AE)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(
                        text = "Calculate Settlement →",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Info Text
            Text(
                text = "This will test:\n• Hilt Dependency Injection\n• Retrofit API Call\n• Clean Architecture\n• MVVM Pattern",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        }

        // Group ID Dialog
        if (showGroupIdDialog) {
            var groupId by remember { mutableStateOf("") }

            AlertDialog(
                onDismissRequest = { showGroupIdDialog = false },
                title = {
                    Text(
                        text = "Enter Group ID",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CB5AE)
                    )
                },
                text = {
                    OutlinedTextField(
                        value = groupId,
                        onValueChange = { groupId = it },
                        label = { Text("Group ID") },
                        placeholder = { Text("e.g., group_1") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4CB5AE),
                            cursorColor = Color(0xFF4CB5AE)
                        )
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (groupId.isNotBlank()) {
                                showGroupIdDialog = false
                                onNavigateToExpenseList(groupId.trim())
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CB5AE)
                        )
                    ) {
                        Text("View")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showGroupIdDialog = false }) {
                        Text("Cancel", color = Color.Gray)
                    }
                }
            )
        }

        // Settlement Dialog
        if (showSettlementDialog) {
            var groupId by remember { mutableStateOf("") }

            AlertDialog(
                onDismissRequest = { showSettlementDialog = false },
                title = {
                    Text(
                        text = "Calculate Settlement",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CB5AE)
                    )
                },
                text = {
                    Column {
                        Text(
                            text = "Enter the group ID to calculate who owes whom",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        OutlinedTextField(
                            value = groupId,
                            onValueChange = { groupId = it },
                            label = { Text("Group ID") },
                            placeholder = { Text("e.g., group_1") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF4CB5AE),
                                cursorColor = Color(0xFF4CB5AE)
                            )
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (groupId.isNotBlank()) {
                                showSettlementDialog = false
                                onNavigateToSettlement(groupId.trim())
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CB5AE)
                        )
                    ) {
                        Text("Calculate")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showSettlementDialog = false }) {
                        Text("Cancel", color = Color.Gray)
                    }
                }
            )
        }

        // Group Details Dialog
        if (showGroupDetailsDialog) {
            var groupId by remember { mutableStateOf("") }

            AlertDialog(
                onDismissRequest = { showGroupDetailsDialog = false },
                title = {
                    Text(
                        text = "View Group Details",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CB5AE)
                    )
                },
                text = {
                    Column {
                        Text(
                            text = "Enter the group ID to view details",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        OutlinedTextField(
                            value = groupId,
                            onValueChange = { groupId = it },
                            label = { Text("Group ID") },
                            placeholder = { Text("e.g., group_1") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF4CB5AE),
                                cursorColor = Color(0xFF4CB5AE)
                            )
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (groupId.isNotBlank()) {
                                showGroupDetailsDialog = false
                                onNavigateToGroupDetails(groupId.trim())
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CB5AE)
                        )
                    ) {
                        Text("View")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showGroupDetailsDialog = false }) {
                        Text("Cancel", color = Color.Gray)
                    }
                }
            )
        }
    }
}

