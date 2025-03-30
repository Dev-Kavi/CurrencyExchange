package com.dev.carl.currencyexchange.ui.converter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.carl.currencyexchange.domain.repository.ExchangeRepository
import com.dev.carl.currencyexchange.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExchangeViewModel @Inject constructor(
    private val repository: ExchangeRepository
) : ViewModel() {

    var state by mutableStateOf(ExchangeState())
       private set

    init {
        loadExchangeRateData()
    }


    private fun loadExchangeRateData() {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error = null
            )
            when (val result = repository.getExchangeRates()) {
                is Resource.Error -> {
                    state = state.copy(
                        ratesData = null,
                        isLoading = false,
                        error = result.message
                    )
                }

                is Resource.Success -> {
                    state = state.copy(
                        ratesData = result.data,
                        isLoading = false,
                        error = null
                    )
                }
            }
        }
    }
}