package com.example.splitwise_final.data.repository

import com.example.splitwise_final.data.remote.ApiService
import com.example.splitwise_final.data.remote.dto.CreateExpenseRequest
import com.example.splitwise_final.data.remote.dto.CreateGroupRequest
import com.example.splitwise_final.data.remote.dto.UserDto
import com.example.splitwise_final.domain.model.Expense
import com.example.splitwise_final.domain.model.ExpenseCategories
import com.example.splitwise_final.domain.model.Group
import com.example.splitwise_final.domain.model.GroupExpenses
import com.example.splitwise_final.domain.model.ReceiptScanResult
import com.example.splitwise_final.domain.model.Settlement
import com.example.splitwise_final.domain.model.SettlementResult
import com.example.splitwise_final.domain.model.User
import com.example.splitwise_final.domain.repository.SettleUpRepository
import com.example.splitwise_final.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SettleUpRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : SettleUpRepository {

    override suspend fun testConnection(): Flow<Resource<String>> = flow {
        emit(Resource.Loading())

        try {
            val response = apiService.testConnection()

            if (response.isSuccessful) {
                val body = response.body()
                emit(Resource.Success(body?.message ?: "Connection successful!"))
            } else {
                emit(Resource.Error("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("HTTP Error: ${e.message()}"))
        } catch (e: IOException) {
            emit(Resource.Error("Network Error: ${e.message ?: "Check your internet connection"}"))
        } catch (e: Exception) {
            emit(Resource.Error("Unexpected Error: ${e.message ?: "Something went wrong"}"))
        }
    }

    override suspend fun getCategories(): Flow<Resource<ExpenseCategories>> = flow {
        emit(Resource.Loading())

        try {
            val response = apiService.getCategories()

            if (response.isSuccessful) {
                val body = response.body()
                if (body?.success == true && body.data != null) {
                    // Parse the data field into ExpenseCategories
                    val dataMap = body.data

                    @Suppress("UNCHECKED_CAST")
                    val categories = dataMap["categories"] as? List<String> ?: emptyList()

                    @Suppress("UNCHECKED_CAST")
                    val examples = dataMap["examples"] as? Map<String, List<String>> ?: emptyMap()

                    val aiPowered = dataMap["ai_powered"] as? Boolean ?: false

                    val expenseCategories = ExpenseCategories(
                        categories = categories,
                        examples = examples,
                        aiPowered = aiPowered
                    )

                    emit(Resource.Success(expenseCategories))
                } else {
                    emit(Resource.Error(body?.message ?: "Failed to fetch categories"))
                }
            } else {
                emit(Resource.Error("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("HTTP Error: ${e.message()}"))
        } catch (e: IOException) {
            emit(Resource.Error("Network Error: ${e.message ?: "Check your internet connection"}"))
        } catch (e: Exception) {
            emit(Resource.Error("Unexpected Error: ${e.message ?: "Something went wrong"}"))
        }
    }

    override suspend fun createGroup(
        groupName: String,
        members: List<User>
    ): Flow<Resource<Group>> = flow {
        emit(Resource.Loading())

        try {
            // Map domain User to UserDto
            val memberDtos = members.map { user ->
                UserDto(
                    id = user.id,
                    name = user.name,
                    email = user.email
                )
            }

            // Create request
            val request = CreateGroupRequest(
                name = groupName,
                members = memberDtos
            )

            val response = apiService.createGroup(request)

            if (response.isSuccessful) {
                val body = response.body()
                if (body?.success == true && body.data != null) {
                    // Parse the group data from response
                    val dataMap = body.data
                    val groupData = dataMap["group"] as? Map<*, *>

                    if (groupData != null) {
                        // Extract group fields
                        val groupId = groupData["id"] as? String ?: ""
                        val groupName = groupData["name"] as? String ?: ""
                        val createdAt = groupData["created_at"] as? String ?: ""
                        val totalExpenses = (groupData["total_expenses"] as? Double)?.toInt() ?: 0
                        val totalAmount = groupData["total_amount"] as? Double ?: 0.0

                        // Extract members
                        @Suppress("UNCHECKED_CAST")
                        val membersData = groupData["members"] as? List<Map<String, Any>> ?: emptyList()
                        val domainMembers = membersData.map { memberMap ->
                            User(
                                id = memberMap["id"] as? String ?: "",
                                name = memberMap["name"] as? String ?: "",
                                email = memberMap["email"] as? String ?: ""
                            )
                        }

                        val group = Group(
                            id = groupId,
                            name = groupName,
                            members = domainMembers,
                            createdAt = createdAt,
                            totalExpenses = totalExpenses,
                            totalAmount = totalAmount
                        )

                        emit(Resource.Success(group))
                    } else {
                        emit(Resource.Error("Invalid group data format"))
                    }
                } else {
                    emit(Resource.Error(body?.message ?: "Failed to create group"))
                }
            } else {
                emit(Resource.Error("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("HTTP Error: ${e.message()}"))
        } catch (e: IOException) {
            emit(Resource.Error("Network Error: ${e.message ?: "Check your internet connection"}"))
        } catch (e: Exception) {
            emit(Resource.Error("Unexpected Error: ${e.message ?: "Something went wrong"}"))
        }
    }

    override suspend fun createExpense(
        description: String,
        amount: Double,
        paidByUserId: String,
        splitAmongUserIds: List<String>,
        groupId: String,
        category: String?
    ): Flow<Resource<Expense>> = flow {
        emit(Resource.Loading())

        try {
            val request = CreateExpenseRequest(
                description = description,
                amount = amount,
                paidByUserId = paidByUserId,
                splitAmongUserIds = splitAmongUserIds,
                groupId = groupId,
                category = category
            )

            val response = apiService.createManualExpense(request)

            if (response.isSuccessful) {
                val body = response.body()
                if (body?.success == true && body.data != null) {
                    val dataMap = body.data
                    val expenseData = dataMap["expense"] as? Map<*, *>

                    if (expenseData != null) {
                        val expenseId = expenseData["id"] as? String ?: ""
                        val expenseDescription = expenseData["description"] as? String ?: ""
                        val expenseAmount = expenseData["amount"] as? Double ?: 0.0
                        val paidBy = expenseData["paid_by_user_id"] as? String ?: ""
                        val expenseCategory = expenseData["category"] as? String ?: ""
                        val createdAt = expenseData["created_at"] as? String ?: ""

                        @Suppress("UNCHECKED_CAST")
                        val splitUsers = expenseData["split_among_user_ids"] as? List<String> ?: emptyList()

                        val expense = Expense(
                            id = expenseId,
                            description = expenseDescription,
                            amount = expenseAmount,
                            paidByUserId = paidBy,
                            splitAmongUserIds = splitUsers,
                            groupId = groupId,
                            category = expenseCategory,
                            createdAt = createdAt
                        )

                        emit(Resource.Success(expense))
                    } else {
                        emit(Resource.Error("Invalid expense data format"))
                    }
                } else {
                    emit(Resource.Error(body?.message ?: "Failed to create expense"))
                }
            } else {
                emit(Resource.Error("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("HTTP Error: ${e.message()}"))
        } catch (e: IOException) {
            emit(Resource.Error("Network Error: ${e.message ?: "Check your internet connection"}"))
        } catch (e: Exception) {
            emit(Resource.Error("Unexpected Error: ${e.message ?: "Something went wrong"}"))
        }
    }

    override suspend fun getGroupExpenses(groupId: String): Flow<Resource<GroupExpenses>> = flow {
        emit(Resource.Loading())

        try {
            val response = apiService.getGroupExpenses(groupId)

            if (response.isSuccessful) {
                val body = response.body()
                if (body?.success == true && body.data != null) {
                    val dataMap = body.data

                    // Parse expenses list
                    @Suppress("UNCHECKED_CAST")
                    val expensesData = dataMap["expenses"] as? List<Map<String, Any>> ?: emptyList()

                    val expenses = expensesData.map { expenseMap ->
                        val id = expenseMap["id"] as? String ?: ""
                        val description = expenseMap["description"] as? String ?: ""
                        val amount = expenseMap["amount"] as? Double ?: 0.0
                        val paidBy = expenseMap["paid_by_user_id"] as? String ?: ""
                        val category = expenseMap["category"] as? String ?: ""
                        val createdAt = expenseMap["created_at"] as? String ?: ""
                        val groupId = expenseMap["group_id"] as? String ?: ""

                        @Suppress("UNCHECKED_CAST")
                        val splitUsers = expenseMap["split_among_user_ids"] as? List<String> ?: emptyList()

                        Expense(
                            id = id,
                            description = description,
                            amount = amount,
                            paidByUserId = paidBy,
                            splitAmongUserIds = splitUsers,
                            groupId = groupId,
                            category = category,
                            createdAt = createdAt
                        )
                    }

                    // Parse category breakdown
                    @Suppress("UNCHECKED_CAST")
                    val categoryBreakdown = dataMap["category_breakdown"] as? Map<String, Double> ?: emptyMap()

                    // Parse total amount
                    val totalAmount = dataMap["total_amount"] as? Double ?: 0.0

                    val groupExpenses = GroupExpenses(
                        expenses = expenses,
                        categoryBreakdown = categoryBreakdown,
                        totalAmount = totalAmount
                    )

                    emit(Resource.Success(groupExpenses))
                } else {
                    emit(Resource.Error(body?.message ?: "Failed to fetch expenses"))
                }
            } else {
                emit(Resource.Error("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("HTTP Error: ${e.message()}"))
        } catch (e: IOException) {
            emit(Resource.Error("Network Error: ${e.message ?: "Check your internet connection"}"))
        } catch (e: Exception) {
            emit(Resource.Error("Unexpected Error: ${e.message ?: "Something went wrong"}"))
        }
    }

    override suspend fun calculateSettlement(groupId: String): Flow<Resource<SettlementResult>> = flow {
        emit(Resource.Loading())

        try {
            val response = apiService.calculateSettlement(groupId)

            if (response.isSuccessful) {
                val body = response.body()
                if (body?.success == true && body.data != null) {
                    val dataMap = body.data

                    // Parse settlements list
                    @Suppress("UNCHECKED_CAST")
                    val settlementsData = dataMap["settlements"] as? List<Map<String, Any>> ?: emptyList()

                    val settlements = settlementsData.map { settlementMap ->
                        val from = settlementMap["from_user_id"] as? String ?: settlementMap["from"] as? String ?: ""
                        val to = settlementMap["to_user_id"] as? String ?: settlementMap["to"] as? String ?: ""
                        val amount = settlementMap["amount"] as? Double ?: 0.0
                        val message = settlementMap["message"] as? String ?: ""

                        // Extract user info
                        val fromUserMap = settlementMap["from_user"] as? Map<*, *>
                        val toUserMap = settlementMap["to_user"] as? Map<*, *>

                        val fromUserName = fromUserMap?.get("name") as? String ?: "User $from"
                        val toUserName = toUserMap?.get("name") as? String ?: "User $to"

                        Settlement(
                            from = from,
                            to = to,
                            amount = amount,
                            fromUserName = fromUserName,
                            toUserName = toUserName,
                            message = message
                        )
                    }

                    // Parse balances
                    @Suppress("UNCHECKED_CAST")
                    val balances = dataMap["balances"] as? Map<String, Double> ?: emptyMap()

                    // Parse total transactions
                    val totalTransactions = (dataMap["total_transactions"] as? Double)?.toInt() ?: settlements.size

                    val settlementResult = SettlementResult(
                        settlements = settlements,
                        balances = balances,
                        totalTransactions = totalTransactions
                    )

                    emit(Resource.Success(settlementResult))
                } else {
                    emit(Resource.Error(body?.message ?: "Failed to calculate settlement"))
                }
            } else {
                emit(Resource.Error("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("HTTP Error: ${e.message()}"))
        } catch (e: IOException) {
            emit(Resource.Error("Network Error: ${e.message ?: "Check your internet connection"}"))
        } catch (e: Exception) {
            emit(Resource.Error("Unexpected Error: ${e.message ?: "Something went wrong"}"))
        }
    }

    override suspend fun getGroupDetails(groupId: String): Flow<Resource<Group>> = flow {
        emit(Resource.Loading())

        try {
            val response = apiService.getGroupDetails(groupId)

            if (response.isSuccessful) {
                val body = response.body()
                if (body?.success == true && body.data != null) {
                    val dataMap = body.data
                    val groupData = dataMap["group"] as? Map<*, *>

                    if (groupData != null) {
                        val id = groupData["id"] as? String ?: ""
                        val name = groupData["name"] as? String ?: ""
                        val createdAt = groupData["created_at"] as? String ?: ""
                        val totalExpenses = (groupData["total_expenses"] as? Double)?.toInt() ?: 0
                        val totalAmount = groupData["total_amount"] as? Double ?: 0.0

                        @Suppress("UNCHECKED_CAST")
                        val membersData = groupData["members"] as? List<Map<String, Any>> ?: emptyList()
                        val members = membersData.map { memberMap ->
                            User(
                                id = memberMap["id"] as? String ?: "",
                                name = memberMap["name"] as? String ?: "",
                                email = memberMap["email"] as? String ?: ""
                            )
                        }

                        val group = Group(
                            id = id,
                            name = name,
                            members = members,
                            createdAt = createdAt,
                            totalExpenses = totalExpenses,
                            totalAmount = totalAmount
                        )

                        emit(Resource.Success(group))
                    } else {
                        emit(Resource.Error("Invalid group data format"))
                    }
                } else {
                    emit(Resource.Error(body?.message ?: "Group not found"))
                }
            } else {
                emit(Resource.Error("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("HTTP Error: ${e.message()}"))
        } catch (e: IOException) {
            emit(Resource.Error("Network Error: ${e.message ?: "Check your internet connection"}"))
        } catch (e: Exception) {
            emit(Resource.Error("Unexpected Error: ${e.message ?: "Something went wrong"}"))
        }
    }

    override suspend fun scanReceipt(
        imageBytes: ByteArray,
        groupId: String,
        paidByUserId: String,
        splitAmongUserIds: List<String>
    ): Flow<Resource<ReceiptScanResult>> = flow {
        emit(Resource.Loading())

        try {
            // Create multipart file part
            val requestFile = imageBytes.toRequestBody("image/*".toMediaTypeOrNull())
            val filePart = MultipartBody.Part.createFormData("file", "receipt.jpg", requestFile)

            // Create request body parts
            val groupIdBody = groupId.toRequestBody("text/plain".toMediaTypeOrNull())
            val paidByBody = paidByUserId.toRequestBody("text/plain".toMediaTypeOrNull())
            val splitUsersJson = JSONArray(splitAmongUserIds).toString()
            val splitUsersBody = splitUsersJson.toRequestBody("text/plain".toMediaTypeOrNull())

            val response = apiService.scanReceipt(
                file = filePart,
                groupId = groupIdBody,
                paidByUserId = paidByBody,
                splitAmongUserIds = splitUsersBody
            )

            if (response.isSuccessful) {
                val body = response.body()
                if (body?.success == true && body.data != null) {
                    val dataMap = body.data

                    // Parse expense data
                    val expenseData = dataMap["expense"] as? Map<*, *>

                    if (expenseData != null) {
                        val expenseId = expenseData["id"] as? String ?: ""
                        val description = expenseData["description"] as? String ?: ""
                        val amount = expenseData["amount"] as? Double ?: 0.0
                        val paidBy = expenseData["paid_by_user_id"] as? String ?: ""
                        val category = expenseData["category"] as? String ?: ""
                        val createdAt = expenseData["created_at"] as? String ?: ""

                        @Suppress("UNCHECKED_CAST")
                        val splitUsers = expenseData["split_among_user_ids"] as? List<String> ?: emptyList()

                        val expense = Expense(
                            id = expenseId,
                            description = description,
                            amount = amount,
                            paidByUserId = paidBy,
                            splitAmongUserIds = splitUsers,
                            groupId = groupId,
                            category = category,
                            createdAt = createdAt
                        )

                        // Parse additional scan data
                        val scannedAmount = dataMap["amount"] as? Double ?: amount
                        val vendor = dataMap["vendor"] as? String ?: "Unknown Vendor"
                        val scanCategory = dataMap["category"] as? String ?: category

                        val receiptScanResult = ReceiptScanResult(
                            expense = expense,
                            scannedAmount = scannedAmount,
                            vendor = vendor,
                            category = scanCategory
                        )

                        emit(Resource.Success(receiptScanResult))
                    } else {
                        emit(Resource.Error("Could not extract receipt data"))
                    }
                } else {
                    emit(Resource.Error(body?.message ?: "Failed to scan receipt"))
                }
            } else {
                emit(Resource.Error("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("HTTP Error: ${e.message()}"))
        } catch (e: IOException) {
            emit(Resource.Error("Network Error: ${e.message ?: "Check your internet connection"}"))
        } catch (e: Exception) {
            emit(Resource.Error("Unexpected Error: ${e.message ?: "Something went wrong"}"))
        }
    }
}
