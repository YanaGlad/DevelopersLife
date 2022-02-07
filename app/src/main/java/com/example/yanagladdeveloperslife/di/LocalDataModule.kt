package com.example.yanagladdeveloperslife.di

import com.example.yanagladdeveloperslife.db.LocalFavouritesDataProvider
import com.example.yanagladdeveloperslife.db.RoomFavouritesDataProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface LocalDataModule {

    @Binds
    @Singleton
    fun bindFavouritesDataProvider(roomFavouritesDataProvider: RoomFavouritesDataProvider): LocalFavouritesDataProvider
}
