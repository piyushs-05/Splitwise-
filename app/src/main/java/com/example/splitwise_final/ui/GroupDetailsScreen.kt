package com.example.splitwise_final.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Data class for expenses
data class Expense(
    val title: String,
    val paidBy: String,
    val amount: String,
    val summary: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDetailsScreen(
    groupName: String = "Trip to Goa",
    members: List<String> = listOf("Anchit", "Rahul", "Priya"),
    onBackClick: () -> Unit = {},
    onAddExpenseClick: () -> Unit = {},
    onViewBalanceClick: () -> Unit = {} // ✅ New callback for balance screen
) {
    val expenses = remember {
        listOf(
            Expense("Dinner at Café 88", "Anchit", "₹1200", "You owe ₹400"),
            Expense("Cab ride", "Rahul", "₹600", "You owe ₹200"),
            Expense("Hotel booking", "Priya", "₹3000", "You are owed ₹500")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = groupName,
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF4CB5AE))
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddExpenseClick,
                containerColor = Color(0xFF4CB5AE)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Expense",
                    tint = Color.White
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF8F8F8))
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Members Section
            Text(
                text = "Members",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                members.forEach { member ->
                    Surface(
                        color = Color(0xFFEAF6F5),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = member,
                            color = Color(0xFF4CB5AE),
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // Expenses Title
            Text(
                text = "Expenses",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            // Expenses List
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false) // ✅ So button sticks to bottom
            ) {
                items(expenses) { expense ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable { /* Navigate to expense details later */ },
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = expense.title,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = expense.amount,
                                    color = Color(0xFF4CB5AE),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Paid by ${expense.paidBy}",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = expense.summary,
                                fontSize = 14.sp,
                                color = if (expense.summary.contains("owed")) Color(0xFF2E7D32) else Color.Red,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ✅ "View Balances" Button
            Button(
                onClick = onViewBalanceClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CB5AE)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .height(50.dp)
            ) {
                Text(
                    text = "View Balances",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GroupDetailsScreenPreview() {
    com.example.splitwise_final.ui.theme.Splitwise_finalTheme {
        GroupDetailsScreen()
    }
}
