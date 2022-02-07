package com.example.yanagladdeveloperslife.network


import com.example.yanagladdeveloperslife.models.response.GifResponse
import com.example.yanagladdeveloperslife.models.response.GifsResponse
import io.reactivex.Flowable
import javax.inject.Inject

class RemoteDataProviderImpl @Inject constructor(private val api: Api) : RemoteDataProvider {

    override fun getRandomGif(): Flowable<GifResponse> = api.getRandomGif()

    override  fun getLatestGifs(page: Int, pageSize: Int, types: String?): Flowable<GifsResponse> =
        api.getLatestGifs(page, pageSize, types)

    override  fun getTopGifs(page: Int, pageSize: Int, types: String?): Flowable<GifsResponse> =
        api.getTopGifs(page, pageSize, types)
}
