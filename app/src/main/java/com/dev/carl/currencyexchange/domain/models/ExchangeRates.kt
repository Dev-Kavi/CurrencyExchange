package com.dev.carl.currencyexchange.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class ExchangeRates(
    val base: String,
    val date: String,
    val rates: Rates
)