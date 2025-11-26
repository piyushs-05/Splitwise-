package com.example.splitwise_final

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.splitwise_final.presentation.addexpense.AddExpenseScreenWrapper
import com.example.splitwise_final.presentation.categories.CategoriesScreen
import com.example.splitwise_final.presentation.creategroup.CreateGroupScreen
import com.example.splitwise_final.presentation.expenselist.ExpenseListScreen
import com.example.splitwise_final.presentation.groupdetails.GroupDetailsScreen
import com.example.splitwise_final.presentation.scanreceipt.ScanReceiptScreenWrapper
import com.example.splitwise_final.presentation.settlement.SettlementScreen
import com.example.splitwise_final.ui.*
import com.example.splitwise_final.ui.theme.Splitwise_finalTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Splitwise_finalTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SplitwiseApp()
                }
            }
        }
    }
}

@Composable
fun SplitwiseApp() {
    var currentScreen by remember { mutableStateOf("splash") }
    var selectedGroupId by remember { mutableStateOf("") }

    when (currentScreen) {
        "splash" -> {
            SplashScreen(
                onSplashFinished = { currentScreen = "home" }
            )
        }
        "home" -> {
            HomeScreen(
                onAddGroupClick = { currentScreen = "creategroup" },
                onGroupClick = { groupId ->
                    selectedGroupId = groupId
                    currentScreen = "groupdetails"
                }
            )
        }
        "creategroup" -> {
            CreateGroupScreen(
                onBackClick = { currentScreen = "home" },
                onGroupCreated = { groupId ->
                    selectedGroupId = groupId
                    currentScreen = "home"
                }
            )
        }
        "groupdetails" -> {
            GroupDetailsScreen(
                groupId = selectedGroupId,
                onBackClick = { currentScreen = "home" },
                onAddExpense = { groupId ->
                    selectedGroupId = groupId
                    currentScreen = "addexpense"
                },
                onScanReceipt = { groupId ->
                    selectedGroupId = groupId
                    currentScreen = "scanreceipt"
                },
                onViewExpenses = { groupId ->
                    selectedGroupId = groupId
                    currentScreen = "expenselist"
                },
                onCalculateSettlement = { groupId ->
                    selectedGroupId = groupId
                    currentScreen = "settlement"
                }
            )
        }
        "addexpense" -> {
            AddExpenseScreenWrapper(
                groupId = selectedGroupId,
                onBackClick = { currentScreen = "groupdetails" },
                onExpenseCreated = { _ ->
                    currentScreen = "groupdetails"
                }
            )
        }
        "scanreceipt" -> {
            ScanReceiptScreenWrapper(
                groupId = selectedGroupId,
                onBackClick = { currentScreen = "groupdetails" },
                onReceiptScanned = { _ ->
                    currentScreen = "groupdetails"
                }
            )
        }
        "expenselist" -> {
            ExpenseListScreen(
                groupId = selectedGroupId,
                onBackClick = { currentScreen = "groupdetails" }
            )
        }
        "settlement" -> {
            SettlementScreen(
                groupId = selectedGroupId,
                onBackClick = { currentScreen = "groupdetails" }
            )
        }
        "categories" -> {
            CategoriesScreen(
                onBackClick = { currentScreen = "home" }
            )
        }
    }
}

