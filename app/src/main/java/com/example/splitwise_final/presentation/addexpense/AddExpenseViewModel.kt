package com.example.splitwise_final.presentation.addexpense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.splitwise_final.domain.model.Expense
import com.example.splitwise_final.domain.repository.SettleUpRepository
import com.example.splitwise_final.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddExpenseState(
    val isLoading: Boolean = false,
    val createdExpense: Expense? = null,
    val error: String = "",
    val description: String = "",
    val amount: String = "",
    val paidByUserId: String = "",
    val splitAmongUserIds: List<String> = emptyList(),
    val groupId: String = "",
    val category: String? = null
)

@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    private val repository: SettleUpRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AddExpenseState())
    val state: StateFlow<AddExpenseState> = _state.asStateFlow()

    fun createExpense(
        description: String,
        amount: Double,
        paidByUserId: String,
        splitAmongUserIds: List<String>,
        groupId: String,
        category: String? = null
    ) {
        viewModelScope.launch {
            repository.createExpense(
                description = description,
                amount = amount,
                paidByUserId = paidByUserId,
                splitAmongUserIds = splitAmongUserIds,
                groupId = groupId,
                category = category
            ).collect { result ->
                _state.value = when (result) {
                    is Resource.Loading -> _state.value.copy(
                        isLoading = true,
                        error = ""
                    )
                    is Resource.Success -> _state.value.copy(
                        isLoading = false,
                        createdExpense = result.data,
                        error = "",
                        description = description,
                        amount = amount.toString(),
                        paidByUserId = paidByUserId,
                        splitAmongUserIds = splitAmongUserIds,
                        groupId = groupId,
                        category = category
                    )
                    is Resource.Error -> _state.value.copy(
                        isLoading = false,
                        error = result.message ?: "Failed to create expense"
                    )
                }
            }
        }
    }

    fun clearState() {
        _state.value = AddExpenseState()
    }
}

