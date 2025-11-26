package com.example.splitwise_final.presentation.scanreceipt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.splitwise_final.domain.model.ReceiptScanResult
import com.example.splitwise_final.domain.repository.SettleUpRepository
import com.example.splitwise_final.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ScanReceiptState(
    val isLoading: Boolean = false,
    val scanResult: ReceiptScanResult? = null,
    val error: String = ""
)

@HiltViewModel
class ScanReceiptViewModel @Inject constructor(
    private val repository: SettleUpRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ScanReceiptState())
    val state: StateFlow<ScanReceiptState> = _state.asStateFlow()

    fun scanReceipt(
        imageBytes: ByteArray,
        groupId: String,
        paidByUserId: String,
        splitAmongUserIds: List<String>
    ) {
        viewModelScope.launch {
            repository.scanReceipt(
                imageBytes = imageBytes,
                groupId = groupId,
                paidByUserId = paidByUserId,
                splitAmongUserIds = splitAmongUserIds
            ).collect { result ->
                _state.value = when (result) {
                    is Resource.Loading -> _state.value.copy(
                        isLoading = true,
                        error = ""
                    )
                    is Resource.Success -> _state.value.copy(
                        isLoading = false,
                        scanResult = result.data,
                        error = ""
                    )
                    is Resource.Error -> _state.value.copy(
                        isLoading = false,
                        error = result.message ?: "Failed to scan receipt"
                    )
                }
            }
        }
    }

    fun clearState() {
        _state.value = ScanReceiptState()
    }
}

