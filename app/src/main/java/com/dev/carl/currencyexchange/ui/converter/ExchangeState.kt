package com.dev.carl.currencyexchange.ui.converter

import com.dev.carl.currencyexchange.domain.models.ExchangeRates

data class ExchangeState(
    val ratesData: ExchangeRates? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)