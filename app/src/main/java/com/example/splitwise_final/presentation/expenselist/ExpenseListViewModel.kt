package com.example.splitwise_final.presentation.expenselist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.splitwise_final.domain.model.GroupExpenses
import com.example.splitwise_final.domain.repository.SettleUpRepository
import com.example.splitwise_final.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExpenseListState(
    val isLoading: Boolean = false,
    val groupExpenses: GroupExpenses? = null,
    val error: String = "",
    val groupId: String = ""
)

@HiltViewModel
class ExpenseListViewModel @Inject constructor(
    private val repository: SettleUpRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ExpenseListState())
    val state: StateFlow<ExpenseListState> = _state.asStateFlow()

    fun setGroupId(groupId: String) {
        _state.value = _state.value.copy(groupId = groupId)
    }

    fun loadExpenses(groupId: String) {
        _state.value = _state.value.copy(groupId = groupId)

        viewModelScope.launch {
            repository.getGroupExpenses(groupId).collect { result ->
                _state.value = when (result) {
                    is Resource.Loading -> _state.value.copy(
                        isLoading = true,
                        error = ""
                    )
                    is Resource.Success -> _state.value.copy(
                        isLoading = false,
                        groupExpenses = result.data,
                        error = ""
                    )
                    is Resource.Error -> _state.value.copy(
                        isLoading = false,
                        error = result.message ?: "Failed to load expenses"
                    )
                }
            }
        }
    }

    fun refresh() {
        if (_state.value.groupId.isNotEmpty()) {
            loadExpenses(_state.value.groupId)
        }
    }
}

