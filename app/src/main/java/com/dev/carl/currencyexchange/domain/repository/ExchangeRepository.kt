package com.dev.carl.currencyexchange.domain.repository

import com.dev.carl.currencyexchange.domain.models.ExchangeRates
import com.dev.carl.currencyexchange.domain.utils.Resource

interface ExchangeRepository {
    suspend fun getExchangeRates() : Resource<ExchangeRates>
}