package com.example.yanagladdeveloperslife.di

import com.example.yanagladdeveloperslife.network.RemoteDataProvider
import com.example.yanagladdeveloperslife.network.RemoteDataProviderImpl
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
    fun bindRemoteFavouritesDataProvider(remoteDataProviderImpl: RemoteDataProviderImpl) : RemoteDataProvider
}
