package com.example.splitwise_final.data.remote

import com.example.splitwise_final.data.remote.dto.ApiResponse
import com.example.splitwise_final.data.remote.dto.CategoriesDto
import com.example.splitwise_final.data.remote.dto.CreateExpenseRequest
import com.example.splitwise_final.data.remote.dto.CreateGroupRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @GET("/")
    suspend fun testConnection(): Response<ApiResponse>

    @GET("/categories")
    suspend fun getCategories(): Response<ApiResponse>

    @POST("/groups/create")
    suspend fun createGroup(@Body request: CreateGroupRequest): Response<ApiResponse>

    @POST("/expenses/manual")
    suspend fun createManualExpense(@Body request: CreateExpenseRequest): Response<ApiResponse>

    @GET("/groups/{group_id}")
    suspend fun getGroupDetails(@Path("group_id") groupId: String): Response<ApiResponse>

    @GET("/groups/{group_id}/expenses")
    suspend fun getGroupExpenses(@Path("group_id") groupId: String): Response<ApiResponse>

    @POST("/groups/{group_id}/calculate-settlement")
    suspend fun calculateSettlement(@Path("group_id") groupId: String): Response<ApiResponse>

    @Multipart
    @POST("/scan-receipt")
    suspend fun scanReceipt(
        @Part file: MultipartBody.Part,
        @Part("group_id") groupId: RequestBody,
        @Part("paid_by_user_id") paidByUserId: RequestBody,
        @Part("split_among_user_ids") splitAmongUserIds: RequestBody
    ): Response<ApiResponse>

    // TODO: Add more endpoints as needed
    // @POST("/api/auth/signin")
    // suspend fun signIn(@Body request: SignInRequest): Response<SignInResponse>

    // @POST("/api/auth/signup")
    // suspend fun signUp(@Body request: SignUpRequest): Response<SignUpResponse>

    // @GET("/api/groups")
    // suspend fun getGroups(): Response<List<GroupDto>>

    // @POST("/api/groups")
    // suspend fun createGroup(@Body request: CreateGroupRequest): Response<GroupDto>

    // @GET("/api/groups/{groupId}")
    // suspend fun getGroupDetails(@Path("groupId") groupId: String): Response<GroupDetailsDto>

    // @POST("/api/expenses")
    // suspend fun addExpense(@Body request: AddExpenseRequest): Response<ExpenseDto>
}

