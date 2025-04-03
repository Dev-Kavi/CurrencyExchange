package com.dev.carl.currencyexchange.ui.converter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.carl.currencyexchange.domain.models.Balances
import com.dev.carl.currencyexchange.domain.models.Rates
import com.dev.carl.currencyexchange.domain.repository.ExchangeRepository
import com.dev.carl.currencyexchange.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExchangeViewModel @Inject constructor(
    private val repository: ExchangeRepository
) : ViewModel() {

    var state by mutableStateOf(ExchangeState())
        private set

    init {
        periodicReFetch()
    }

    private fun periodicReFetch() {
        flow {
            while (true) {
                emit(Unit)
                delay(5000)
            }
        }
            .onEach { loadExchangeRateData() }
            .catch { e ->
                e.printStackTrace()
                state = state.copy(error = "Failed to fetch exchange rates: ${e.message}")
            }
            .launchIn(viewModelScope)
    }

    fun updateFromCurrency(currency: String) {
        state = state.copy(fromCurrency = currency)
    }

    fun updateToCurrency(currency: String) {
        state = state.copy(toCurrency = currency)
    }

    fun convertCurrency(amount: Double) {
        val fromCurrency = state.fromCurrency
        val toCurrency = state.toCurrency
        val rates = state.ratesData?.rates ?: return

        val fromBalance = state.balances.find { it.currency == fromCurrency }?.amount ?: 0.0

        val isFreeExchange = state.exchangeCount < 5
        val commissionRate = if (isFreeExchange) 0.0 else 0.007
        val commission = amount * commissionRate
        val totalRequired = amount + commission

        if (fromBalance < totalRequired) {
            state = state.copy(error = "Insufficient balance in $fromCurrency to cover the amount and commission.")
            return
        }

        val rate = getConversionRate(rates, fromCurrency, toCurrency)

        val netConvertedAmount = amount * rate

        val updatedBalances = state.balances.map { it.copy() }.toMutableList().apply {
            find { it.currency == fromCurrency }?.let {
                it.amount -= totalRequired // Deduct both amount and commission from the balance
            }
            val toBalance = find { it.currency == toCurrency }
            if (toBalance != null) {
                toBalance.amount += netConvertedAmount
            } else {
                add(Balances(netConvertedAmount, toCurrency))
            }
        }

        val conversionDetails = buildString {
            append("You have converted ${"%.2f".format(amount)} $fromCurrency ")
            append("to ${"%.2f".format(netConvertedAmount)} $toCurrency.\n")
            if (!isFreeExchange) {
                append("Commission Fee: ${"%.2f".format(commission)} $fromCurrency.")
            }
        }

        state = state.copy(
            balances = updatedBalances,
            exchangeCount = state.exchangeCount + 1,
            conversionDetails = conversionDetails,
            lastConvertedAmount = netConvertedAmount,
            error = null
        )
    }

    private fun getConversionRate(rates: Rates, from: String, to: String): Double {
        val baseRate = when (from) {
            "EUR" -> 1.0
            else -> rates.getRate(from)
        }
        val targetRate = when (to) {
            "EUR" -> 1.0
            else -> rates.getRate(to)
        }
        return targetRate / baseRate
    }

    fun dismissConversionDialog() {
        state = state.copy(conversionDetails = "")
    }

    private fun loadExchangeRateData() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
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