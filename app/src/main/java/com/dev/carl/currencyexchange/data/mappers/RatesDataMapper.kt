package com.dev.carl.currencyexchange.data.mappers

import com.dev.carl.currencyexchange.data.remote.models.ExchangeRatesDto
import com.dev.carl.currencyexchange.domain.models.ExchangeRates
import com.dev.carl.currencyexchange.domain.models.Rates

fun ExchangeRatesDto.toExchangeRates(): ExchangeRates {
    return ExchangeRates(
        base = this.base,
        date = this.date,
        rates = Rates(this.rates)
    )
}