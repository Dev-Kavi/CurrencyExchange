package com.dev.carl.currencyexchange.data.remote.api

import com.dev.carl.currencyexchange.data.remote.models.ExchangeRatesDto
import retrofit2.http.GET

interface ExchangeApi {
    @GET("currency-exchange-rates")
    suspend fun getExchangeRates() : ExchangeRatesDto
}