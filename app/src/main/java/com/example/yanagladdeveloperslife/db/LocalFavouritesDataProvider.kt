package com.example.yanagladdeveloperslife.db

import androidx.room.Delete
import androidx.room.Query
import com.example.yanagladdeveloperslife.db.enitity.GifEntity
import com.example.yanagladdeveloperslife.models.GifModel
import io.reactivex.Flowable
import io.reactivex.Single

interface LocalFavouritesDataProvider {
    fun insertFavourite(item : GifModel)
    fun getAllFavourites() : Flowable<List<GifModel>>
    fun deleteFavourite(item : GifModel)
    fun getGifByDescription(id : Long) : Single<GifModel>
}