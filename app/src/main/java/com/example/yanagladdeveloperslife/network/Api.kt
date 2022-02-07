package com.example.yanagladdeveloperslife.network

import com.example.yanagladdeveloperslife.models.response.GifResponse
import com.example.yanagladdeveloperslife.models.response.GifsResponse
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("/random?json=true")
    fun getRandomGif(): Flowable<GifResponse>

    @GET("/latest/{page}?json=true")
    fun getLatestGifs(
        @Path(value = "page", encoded = true) page: Int,
        @Query(value = "pageSize", encoded = true) pageSize: Int,
        @Query(value = "types", encoded = true) types: String?
    ): Flowable<GifsResponse>


    @GET("/top/{page}?json=true")
    fun getTopGifs(
        @Path(value = "page", encoded = true) page: Int,
        @Query(value = "pageSize", encoded = true) pageSize: Int,
        @Query(value = "types", encoded = true) types: String?
    ): Flowable<GifsResponse>
}
