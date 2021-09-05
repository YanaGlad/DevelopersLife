package com.example.yanagladdeveloperslife.network

import com.example.yanagladdeveloperslife.models.response.GifResponse
import com.example.yanagladdeveloperslife.models.response.GifsResponse
import io.reactivex.Flowable

interface RemoteDataProvider {
    fun getRandomGif(): Flowable<GifResponse>
    fun getLatestGifs(page: Int, pageSize: Int, types: String?): Flowable<GifsResponse>
    fun getTopGifs(page: Int, pageSize: Int, types: String?): Flowable<GifsResponse>
}