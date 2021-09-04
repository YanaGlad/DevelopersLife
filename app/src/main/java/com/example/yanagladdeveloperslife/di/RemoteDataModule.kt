package com.example.yanagladdeveloperslife.di

import com.example.yanagladdeveloperslife.api.RemoteDataProvider
import com.example.yanagladdeveloperslife.api.RemoteDataProviderImpl
import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RemoteDataModule {

    @Binds
    @Reusable
    fun bindRemoteWalletDataProvider(remoteDataProviderImpl: RemoteDataProviderImpl) : RemoteDataProvider

}