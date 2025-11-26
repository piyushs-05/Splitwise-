package com.example.splitwise_final.di

import com.example.splitwise_final.data.repository.SettleUpRepositoryImpl
import com.example.splitwise_final.domain.repository.SettleUpRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSettleUpRepository(
        settleUpRepositoryImpl: SettleUpRepositoryImpl
    ): SettleUpRepository
}

