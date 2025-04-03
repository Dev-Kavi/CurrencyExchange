package com.dev.carl.currencyexchange.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Rates(val rates: Map<String, Double>) {
    fun getRate(currency: String): Double {
        return rates[currency] ?: throw IllegalArgumentException("Unknown currency: $currency")
    }

    fun getCurrencies(): List<String> {
        return rates.keys.toList()
    }
}