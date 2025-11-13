package com.example.splitwise_final.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAddGroupClick: () -> Unit = {},
    onGroupClick: (String) -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(0) }

    val groups = remember {
        mutableStateListOf("Trip to Goa", "Flatmates", "Office Lunch", "Weekend Party")
    }
    val balanceStatus = "You owe ₹200"

    Scaffold(
        floatingActionButton = {
            if (selectedTab == 0) {
                FloatingActionButton(
                    onClick = onAddGroupClick,
                    containerColor = Color(0xFF4CB5AE)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Group", tint = Color.White)
                }
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (selectedTab == 0) "Dashboard" else "Profile",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF4CB5AE)
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                contentColor = Color(0xFF4CB5AE)
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Dashboard"
                        )
                    },
                    label = { Text("Dashboard") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF4CB5AE),
                        selectedTextColor = Color(0xFF4CB5AE)
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile"
                        )
                    },
                    label = { Text("Profile") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF4CB5AE),
                        selectedTextColor = Color(0xFF4CB5AE)
                    )
                )
            }
        }
    ) { innerPadding ->
        when (selectedTab) {
            0 -> DashboardScreen(
                groups = groups,
                balanceStatus = balanceStatus,
                innerPadding = innerPadding,
                onGroupClick = onGroupClick
            )
            1 -> ProfileScreen(innerPadding = innerPadding)
        }
    }
}

@Composable
fun DashboardScreen(
    groups: List<String>,
    balanceStatus: String,
    innerPadding: PaddingValues,
    onGroupClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(Color(0xFFF8F8F8))
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Balance Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF6F5)),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Text(
                text = balanceStatus,
                color = Color(0xFF4CB5AE),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Groups",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(groups) { group ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable { onGroupClick(group) },
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(10.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = group,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "2 members",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                        Text(
                            text = "₹0",
                            color = Color(0xFF4CB5AE),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(innerPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(Color(0xFFF8F8F8)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Profile",
            tint = Color(0xFF4CB5AE),
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Anchit Dixit",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "anchit@example.com",
            color = Color.Gray,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { /* TODO: Implement logout later */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CB5AE))
        ) {
            Text("Logout", color = Color.White)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    com.example.splitwise_final.ui.theme.Splitwise_finalTheme {
        HomeScreen()
    }
}
