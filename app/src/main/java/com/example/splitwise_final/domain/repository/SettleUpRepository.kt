package com.example.splitwise_final.domain.repository

import com.example.splitwise_final.domain.model.Expense
import com.example.splitwise_final.domain.model.ExpenseCategories
import com.example.splitwise_final.domain.model.Group
import com.example.splitwise_final.domain.model.GroupExpenses
import com.example.splitwise_final.domain.model.User
import com.example.splitwise_final.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface SettleUpRepository {

    suspend fun testConnection(): Flow<Resource<String>>

    suspend fun getCategories(): Flow<Resource<ExpenseCategories>>

    suspend fun createGroup(groupName: String, members: List<User>): Flow<Resource<Group>>

    suspend fun getGroupDetails(groupId: String): Flow<Resource<Group>>

    suspend fun createExpense(
        description: String,
        amount: Double,
        paidByUserId: String,
        splitAmongUserIds: List<String>,
        groupId: String,
        category: String? = null
    ): Flow<Resource<Expense>>

    suspend fun getGroupExpenses(groupId: String): Flow<Resource<GroupExpenses>>

    suspend fun calculateSettlement(groupId: String): Flow<Resource<com.example.splitwise_final.domain.model.SettlementResult>>

    suspend fun scanReceipt(
        imageBytes: ByteArray,
        groupId: String,
        paidByUserId: String,
        splitAmongUserIds: List<String>
    ): Flow<Resource<com.example.splitwise_final.domain.model.ReceiptScanResult>>

    // TODO: Add more repository methods
    // suspend fun signIn(email: String, password: String): Flow<Resource<User>>
    // suspend fun signUp(name: String, email: String): Flow<Resource<User>>
    // suspend fun getGroups(): Flow<Resource<List<Group>>>
    // suspend fun createGroup(name: String, members: List<String>): Flow<Resource<Group>>
    // suspend fun getGroupDetails(groupId: String): Flow<Resource<GroupDetails>>
    // suspend fun addExpense(expense: Expense): Flow<Resource<Expense>>
}

