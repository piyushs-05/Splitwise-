package com.example.splitwise_final.presentation.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.splitwise_final.domain.model.ExpenseCategories
import com.example.splitwise_final.domain.repository.SettleUpRepository
import com.example.splitwise_final.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CategoriesState(
    val isLoading: Boolean = false,
    val categories: ExpenseCategories? = null,
    val error: String = ""
)

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val repository: SettleUpRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CategoriesState())
    val state: StateFlow<CategoriesState> = _state.asStateFlow()

    init {
        // Load categories when ViewModel is created
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            repository.getCategories().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.value = CategoriesState(isLoading = true)
                    }
                    is Resource.Success -> {
                        _state.value = CategoriesState(
                            isLoading = false,
                            categories = result.data
                        )
                    }
                    is Resource.Error -> {
                        _state.value = CategoriesState(
                            isLoading = false,
                            error = result.message ?: "Unknown error"
                        )
                    }
                }
            }
        }
    }
}

