package com.dev.carl.currencyexchange.data.remote.repository

import com.dev.carl.currencyexchange.data.mappers.toExchangeRates
import com.dev.carl.currencyexchange.data.remote.api.ExchangeApi
import com.dev.carl.currencyexchange.domain.models.ExchangeRates
import com.dev.carl.currencyexchange.domain.repository.ExchangeRepository
import com.dev.carl.currencyexchange.domain.utils.Resource
import javax.inject.Inject

class ExchangeRepositoryImp @Inject constructor(
    private val api: ExchangeApi
) : ExchangeRepository {
    override suspend fun getExchangeRates(): Resource<ExchangeRates> {
        return try {
            val response = api.getExchangeRates()
            Resource.Success(response.toExchangeRates())
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }
}