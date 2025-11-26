package com.example.splitwise_final.presentation.creategroup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.splitwise_final.data.local.GroupCache
import com.example.splitwise_final.domain.model.Group
import com.example.splitwise_final.domain.model.User
import com.example.splitwise_final.domain.repository.SettleUpRepository
import com.example.splitwise_final.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CreateGroupState(
    val isLoading: Boolean = false,
    val createdGroup: Group? = null,
    val error: String = "",
    val groupName: String = "",
    val members: List<User> = emptyList()
)

@HiltViewModel
class CreateGroupViewModel @Inject constructor(
    private val repository: SettleUpRepository,
    private val groupCache: GroupCache
) : ViewModel() {

    private val _state = MutableStateFlow(CreateGroupState())
    val state: StateFlow<CreateGroupState> = _state.asStateFlow()

    fun updateGroupName(name: String) {
        _state.value = _state.value.copy(groupName = name)
    }

    fun addMember(user: User) {
        val currentMembers = _state.value.members.toMutableList()
        if (currentMembers.none { it.id == user.id }) {
            currentMembers.add(user)
            _state.value = _state.value.copy(members = currentMembers)
        }
    }

    fun removeMember(userId: String) {
        val updatedMembers = _state.value.members.filter { it.id != userId }
        _state.value = _state.value.copy(members = updatedMembers)
    }

    fun createGroup() {
        val groupName = _state.value.groupName.trim()
        val members = _state.value.members

        if (groupName.isEmpty()) {
            _state.value = _state.value.copy(error = "Group name cannot be empty")
            return
        }

        if (members.size < 2) {
            _state.value = _state.value.copy(error = "Add at least 2 members")
            return
        }

        viewModelScope.launch {
            repository.createGroup(groupName, members).collect { result ->
                _state.value = when (result) {
                    is Resource.Loading -> _state.value.copy(
                        isLoading = true,
                        error = ""
                    )
                    is Resource.Success -> {
                        // Add the created group ID to cache
                        result.data?.id?.let { groupCache.addGroupId(it) }

                        _state.value.copy(
                            isLoading = false,
                            createdGroup = result.data,
                            error = ""
                        )
                    }
                    is Resource.Error -> _state.value.copy(
                        isLoading = false,
                        error = result.message ?: "Failed to create group"
                    )
                }
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = "")
    }

    fun resetState() {
        _state.value = CreateGroupState()
    }
}

