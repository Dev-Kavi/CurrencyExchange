package com.dev.carl.currencyexchange.data.remote.models

import com.squareup.moshi.Json

data class RatesDto(
    @field:Json(name = "rates") val rates: Map<String, Double>?
)