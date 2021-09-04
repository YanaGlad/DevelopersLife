package com.example.yanagladdeveloperslife.api


import com.example.yanagladdeveloperslife.models.Gif
import com.example.yanagladdeveloperslife.models.Gifs
import io.reactivex.Flowable
import retrofit2.Response
import javax.inject.Inject

class RemoteDataProviderImpl @Inject constructor(private val api: Api) :
    RemoteDataProvider {

    override fun getRandomGif(): Flowable<Gif> = api.getRandomGif()

    override  fun getLatestGifs(page: Int, pageSize: Int, types: String?): Flowable<Gifs> =
        api.getLatestGifs(page, pageSize, types)

    override  fun getTopGifs(page: Int, pageSize: Int, types: String?): Flowable<Gifs> =
        api.getTopGifs(page, pageSize, types)
}