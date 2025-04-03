package com.dev.carl.currencyexchange.data.remote.models

import com.squareup.moshi.Json

data class ExchangeRatesDto(
    @field:Json(name = "base") val base: String,
    @field:Json(name = "date") val date: String,
    @field:Json(name = "rates") val rates: Map<String, Double>
)