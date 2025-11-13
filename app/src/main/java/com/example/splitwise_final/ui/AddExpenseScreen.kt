package com.example.splitwise_final.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.rotate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    members: List<String> = listOf("Anchit", "Rahul", "Priya"),
    onBackClick: () -> Unit = {},
    onAddExpenseClick: (String, String, String, List<String>) -> Unit = { _, _, _, _ -> }
) {
    var expenseTitle by remember { mutableStateOf(TextFieldValue("")) }
    var amount by remember { mutableStateOf(TextFieldValue("")) }
    var paidBy by remember { mutableStateOf(members.first()) }
    val selectedMembers = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = "Add Expense",
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

        Spacer(modifier = Modifier.height(24.dp))

        // Expense Title Field
        OutlinedTextField(
            value = expenseTitle,
            onValueChange = { expenseTitle = it },
            label = { Text("Expense title (e.g. Dinner at Café 88)") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Amount Field
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Paid By Dropdown
        Text(
            text = "Paid by",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, bottom = 4.dp)
        )

        var expanded by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            OutlinedTextField(
                value = paidBy,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select payer") },
                trailingIcon = {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier.rotate(if (expanded) 90f else 270f),
                            tint = Color.Gray
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                members.forEach { member ->
                    DropdownMenuItem(
                        text = { Text(member) },
                        onClick = {
                            paidBy = member
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Split Between Section
        Text(
            text = "Split between",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, bottom = 8.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            members.forEach { member ->j
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            if (selectedMembers.contains(member))
                                selectedMembers.remove(member)
                            else
                                selectedMembers.add(member)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = selectedMembers.contains(member),
                        onCheckedChange = {
                            if (it) selectedMembers.add(member)
                            else selectedMembers.remove(member)
                        },
                        colors = CheckboxDefaults.colors(checkedColor = Color(0xFF4CB5AE))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = member, fontSize = 16.sp, color = Color.Black)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Add Expense Button
        Button(
            onClick = {
                onAddExpenseClick(
                    expenseTitle.text,
                    amount.text,
                    paidBy,
                    selectedMembers
                )
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CB5AE)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(50.dp)
        ) {
            Text(
                text = "Add Expense",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddExpenseScreenPreview() {
    com.example.splitwise_final.ui.theme.Splitwise_finalTheme {
        AddExpenseScreen()
    }
}
