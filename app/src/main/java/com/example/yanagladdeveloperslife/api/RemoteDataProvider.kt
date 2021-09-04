package com.example.yanagladdeveloperslife.api

import com.example.yanagladdeveloperslife.models.Gif
import com.example.yanagladdeveloperslife.models.Gifs
import io.reactivex.Flowable

interface RemoteDataProvider {
    fun getRandomGif(): Flowable<Gif>
    fun getLatestGifs(page: Int, pageSize: Int, types: String?): Flowable<Gifs>
    fun getTopGifs(page: Int, pageSize: Int, types: String?): Flowable<Gifs>
}