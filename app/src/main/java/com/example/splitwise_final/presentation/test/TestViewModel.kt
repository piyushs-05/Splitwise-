package com.example.splitwise_final.presentation.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.splitwise_final.domain.repository.SettleUpRepository
import com.example.splitwise_final.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TestConnectionState(
    val isLoading: Boolean = false,
    val message: String = "",
    val error: String = ""
)

@HiltViewModel
class TestViewModel @Inject constructor(
    private val repository: SettleUpRepository
) : ViewModel() {

    private val _state = MutableStateFlow(TestConnectionState())
    val state: StateFlow<TestConnectionState> = _state.asStateFlow()

    fun testConnection() {
        viewModelScope.launch {
            repository.testConnection().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.value = TestConnectionState(isLoading = true)
                    }
                    is Resource.Success -> {
                        _state.value = TestConnectionState(
                            isLoading = false,
                            message = result.data ?: "Success!"
                        )
                    }
                    is Resource.Error -> {
                        _state.value = TestConnectionState(
                            isLoading = false,
                            error = result.message ?: "Unknown error"
                        )
                    }
                }
            }
        }
    }
}

