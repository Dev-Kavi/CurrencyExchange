package com.dev.carl.currencyexchange.di

import com.dev.carl.currencyexchange.data.remote.repository.ExchangeRepositoryImp
import com.dev.carl.currencyexchange.domain.repository.ExchangeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindExchangeRepository(
        exchangeRepositoryImp: ExchangeRepositoryImp
    ) : ExchangeRepository
}