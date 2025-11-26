package com.example.splitwise_final.presentation.groupdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.splitwise_final.domain.model.Group
import com.example.splitwise_final.domain.repository.SettleUpRepository
import com.example.splitwise_final.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GroupDetailsState(
    val isLoading: Boolean = false,
    val group: Group? = null,
    val error: String = "",
    val groupId: String = ""
)

@HiltViewModel
class GroupDetailsViewModel @Inject constructor(
    private val repository: SettleUpRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GroupDetailsState())
    val state: StateFlow<GroupDetailsState> = _state.asStateFlow()

    fun loadGroupDetails(groupId: String) {
        _state.value = _state.value.copy(groupId = groupId)

        viewModelScope.launch {
            repository.getGroupDetails(groupId).collect { result ->
                _state.value = when (result) {
                    is Resource.Loading -> _state.value.copy(
                        isLoading = true,
                        error = ""
                    )
                    is Resource.Success -> _state.value.copy(
                        isLoading = false,
                        group = result.data,
                        error = ""
                    )
                    is Resource.Error -> _state.value.copy(
                        isLoading = false,
                        error = result.message ?: "Failed to load group details"
                    )
                }
            }
        }
    }

    fun refresh() {
        if (_state.value.groupId.isNotEmpty()) {
            loadGroupDetails(_state.value.groupId)
        }
    }
}

