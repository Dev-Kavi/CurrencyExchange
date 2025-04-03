package com.dev.carl.currencyexchange.ui.converter

import com.dev.carl.currencyexchange.common.Constants.DEFAULT_CURRENCY
import com.dev.carl.currencyexchange.domain.models.Balances
import com.dev.carl.currencyexchange.domain.models.ExchangeRates

val DEFAULT_BALANCES = mutableListOf(Balances(amount = 1000.00, currency = DEFAULT_CURRENCY))

data class ExchangeState(
    val ratesData: ExchangeRates? = null,
    val fromCurrency: String = DEFAULT_CURRENCY,
    val toCurrency: String = DEFAULT_CURRENCY,
    val balances: MutableList<Balances> = DEFAULT_BALANCES.toMutableList(),
    val exchangeCount: Int = 0,
    val conversionDetails: String = "",
    val lastConvertedAmount: Double = 0.0,
    val isLoading: Boolean = false,
    val error: String? = null,
)