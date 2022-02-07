package com.example.yanagladdeveloperslife.di

import android.content.Context
import androidx.room.Room
import com.example.yanagladdeveloperslife.db.FavouritesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

    @Provides
    @Singleton
    fun provideFavouritesDatabase(@ApplicationContext context : Context) : FavouritesDatabase =
        Room.databaseBuilder(context, FavouritesDatabase::class.java, "fav-db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideGifsDao(database: FavouritesDatabase) = database.gifDao()
}
