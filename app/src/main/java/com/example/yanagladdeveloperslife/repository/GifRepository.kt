package com.example.yanagladdeveloperslife.repository

import com.example.yanagladdeveloperslife.models.GifModel
import com.example.yanagladdeveloperslife.viewstate.RandomGifViewState
import com.example.yanagladdeveloperslife.viewstate.RecyclerGifViewState
import io.reactivex.Flowable
import io.reactivex.Single

interface GifRepository {
    fun getRandomGif(): Single<RandomGifViewState>
    fun getLatestGifs(page: Int, pageSize: Int, types: String?): Single<RecyclerGifViewState>
    fun getTopGifs(page: Int, pageSize: Int, types: String?): Single<RecyclerGifViewState>
    fun addGifToFavourites(gifModel: GifModel)
    fun getFavourites() : Flowable<List<GifModel>>
    fun getFavById(gifModel: GifModel) : Single<GifModel>
    fun deleteFavourite(item : GifModel)
    fun dispose()
}
