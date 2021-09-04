package com.example.yanagladdeveloperslife.db

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.yanagladdeveloperslife.db.enitity.GifEntity
import com.example.yanagladdeveloperslife.models.GifModel
import io.reactivex.Flowable

interface LocalFavouritesDataProvider {
    fun insertFavourite(item : GifModel)
    fun getAllFavourites() : Flowable<List<GifModel>>
    fun deleteFavourite(item : GifModel)
}