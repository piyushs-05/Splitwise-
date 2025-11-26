package com.example.splitwise_final.presentation.addexpense

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    groupId: String = "group_1",
    members: List<com.example.splitwise_final.domain.model.User> = emptyList(),
    onBackClick: () -> Unit = {},
    onExpenseCreated: (String) -> Unit = {},
    viewModel: AddExpenseViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    var description by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var paidBy by remember { mutableStateOf(members.firstOrNull()?.id ?: "") }
    var selectedUsers by remember { mutableStateOf(members.firstOrNull()?.id?.let { setOf(it) } ?: emptySet()) }
    var selectedCategory by remember { mutableStateOf("Food & Dining") }
    var showSuccess by remember { mutableStateOf(false) }

    // Show success dialog
    if (state.createdExpense != null && !showSuccess) {
        showSuccess = true
    }

    if (showSuccess) {
        AlertDialog(
            onDismissRequest = {
                showSuccess = false
                viewModel.clearState()
                onExpenseCreated(state.createdExpense?.id ?: "")
            },
            title = {
                Text("Success!", fontWeight = FontWeight.Bold, color = Color(0xFF4CB5AE))
            },
            text = {
                Text("Expense created successfully!")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccess = false
                        viewModel.clearState()
                        onExpenseCreated(state.createdExpense?.id ?: "")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CB5AE)
                    )
                ) {
                    Text("OK")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Add Expense",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF4CB5AE)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F8F8))
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Description
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                placeholder = { Text("e.g., Dinner at Restaurant") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4CB5AE),
                    cursorColor = Color(0xFF4CB5AE)
                )
            )

            // Amount
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount (₹)") },
                placeholder = { Text("0.00") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4CB5AE),
                    cursorColor = Color(0xFF4CB5AE)
                )
            )

            // Category
            var expandedCategory by remember { mutableStateOf(false) }
            val categories = listOf(
                "Food & Dining",
                "Transportation",
                "Entertainment",
                "Shopping",
                "Bills & Utilities",
                "Travel",
                "Healthcare",
                "Gifts & Miscellaneous"
            )

            ExposedDropdownMenuBox(
                expanded = expandedCategory,
                onExpandedChange = { expandedCategory = it }
            ) {
                OutlinedTextField(
                    value = selectedCategory,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF4CB5AE),
                        cursorColor = Color(0xFF4CB5AE)
                    )
                )
                ExposedDropdownMenu(
                    expanded = expandedCategory,
                    onDismissRequest = { expandedCategory = false }
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category) },
                            onClick = {
                                selectedCategory = category
                                expandedCategory = false
                            }
                        )
                    }
                }
            }

            // Paid By
            var expandedPaidBy by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                expanded = expandedPaidBy,
                onExpandedChange = { expandedPaidBy = it }
            ) {
                OutlinedTextField(
                    value = members.find { it.id == paidBy }?.name ?: "Select user",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Paid By") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPaidBy) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF4CB5AE),
                        cursorColor = Color(0xFF4CB5AE)
                    )
                )
                ExposedDropdownMenu(
                    expanded = expandedPaidBy,
                    onDismissRequest = { expandedPaidBy = false }
                ) {
                    members.forEach { member ->
                        DropdownMenuItem(
                            text = { Text(member.name) },
                            onClick = {
                                paidBy = member.id
                                expandedPaidBy = false
                            }
                        )
                    }
                }
            }

            // Split Among
            Text(
                text = "Split Among",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray
            )

            if (members.isEmpty()) {
                Text(
                    text = "No members available. Please add members to the group.",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                members.forEach { member ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = selectedUsers.contains(member.id),
                            onCheckedChange = { checked ->
                                selectedUsers = if (checked) {
                                    selectedUsers + member.id
                                } else {
                                    selectedUsers - member.id
                                }
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xFF4CB5AE)
                            )
                        )
                        Text(
                            text = member.name,
                            fontSize = 16.sp,
                            color = Color.DarkGray
                        )
                    }
                }
            }

            // Error message
            if (state.error.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFEBEE)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = state.error,
                        color = Color(0xFFC62828),
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Add Expense Button
            Button(
                onClick = {
                    val amountValue = amount.toDoubleOrNull()
                    if (description.isNotBlank() && amountValue != null && amountValue > 0 && selectedUsers.isNotEmpty()) {
                        viewModel.createExpense(
                            description = description,
                            amount = amountValue,
                            paidByUserId = paidBy,
                            splitAmongUserIds = selectedUsers.toList(),
                            groupId = groupId,
                            category = selectedCategory
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CB5AE)
                ),
                shape = RoundedCornerShape(8.dp),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = "Add Expense",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

