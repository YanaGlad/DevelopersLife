package com.example.yanagladdeveloperslife.repository

import com.example.yanagladdeveloperslife.viewstate.RandomGifViewState
import com.example.yanagladdeveloperslife.viewstate.RecyclerGifViewState
import io.reactivex.Single

interface GifRepository {
    fun getRandomGif(): Single<RandomGifViewState>
    fun getLatestGifs(page: Int, pageSize: Int, types: String?): Single<RecyclerGifViewState>
    fun getTopGifs(page: Int, pageSize: Int, types: String?): Single<RecyclerGifViewState>
}