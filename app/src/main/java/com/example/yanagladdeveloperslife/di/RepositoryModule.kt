package com.example.yanagladdeveloperslife.di

import com.example.yanagladdeveloperslife.repository.GifRepository
import com.example.yanagladdeveloperslife.repository.GifRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Singleton
    @Binds
    fun bindGifsRepository(gifRepositoryImpl: GifRepositoryImpl): GifRepository
}
