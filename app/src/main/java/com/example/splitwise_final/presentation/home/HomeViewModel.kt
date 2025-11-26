package com.example.splitwise_final.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.splitwise_final.data.local.GroupCache
import com.example.splitwise_final.domain.model.Group
import com.example.splitwise_final.domain.repository.SettleUpRepository
import com.example.splitwise_final.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeState(
    val isLoading: Boolean = false,
    val groups: List<Group> = emptyList(),
    val error: String = ""
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: SettleUpRepository,
    private val groupCache: GroupCache
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        // Load groups on startup
        loadGroups()
    }

    fun loadGroups() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = "")

            // Get group IDs from cache, or use defaults if cache is empty
            val groupIds = if (groupCache.hasGroups()) {
                groupCache.getGroupIds()
            } else {
                // Default demo groups - add them to cache
                val defaults = listOf("group_1", "group_2", "group_3", "group_4")
                defaults.forEach { groupCache.addGroupId(it) }
                defaults
            }

            val groups = mutableListOf<Group>()
            var failedCount = 0

            for (groupId in groupIds) {
                try {
                    repository.getGroupDetails(groupId).collect { result ->
                        when (result) {
                            is Resource.Success -> {
                                result.data?.let {
                                    groups.add(it)
                                }
                            }
                            is Resource.Error -> {
                                failedCount++
                            }
                            is Resource.Loading -> {
                                // Keep loading state
                            }
                        }
                    }
                } catch (e: Exception) {
                    failedCount++
                }
            }

            // Show error only if all groups failed to load
            val shouldShowError = groups.isEmpty() && failedCount > 0

            _state.value = _state.value.copy(
                isLoading = false,
                groups = groups.sortedBy { it.name },
                error = if (shouldShowError) "Unable to load groups. Please check your backend connection." else ""
            )
        }
    }

    fun addGroup(groupId: String) {
        groupCache.addGroupId(groupId)
        refresh()
    }

    fun refresh() {
        loadGroups()
    }
}

