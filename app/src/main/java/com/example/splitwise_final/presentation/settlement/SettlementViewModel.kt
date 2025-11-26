package com.example.splitwise_final.presentation.settlement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.splitwise_final.domain.model.SettlementResult
import com.example.splitwise_final.domain.repository.SettleUpRepository
import com.example.splitwise_final.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettlementState(
    val isLoading: Boolean = false,
    val settlementResult: SettlementResult? = null,
    val error: String = "",
    val groupId: String = ""
)

@HiltViewModel
class SettlementViewModel @Inject constructor(
    private val repository: SettleUpRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SettlementState())
    val state: StateFlow<SettlementState> = _state.asStateFlow()

    fun calculateSettlement(groupId: String) {
        _state.value = _state.value.copy(groupId = groupId)

        viewModelScope.launch {
            repository.calculateSettlement(groupId).collect { result ->
                _state.value = when (result) {
                    is Resource.Loading -> _state.value.copy(
                        isLoading = true,
                        error = ""
                    )
                    is Resource.Success -> _state.value.copy(
                        isLoading = false,
                        settlementResult = result.data,
                        error = ""
                    )
                    is Resource.Error -> _state.value.copy(
                        isLoading = false,
                        error = result.message ?: "Failed to calculate settlement"
                    )
                }
            }
        }
    }

    fun refresh() {
        if (_state.value.groupId.isNotEmpty()) {
            calculateSettlement(_state.value.groupId)
        }
    }
}

