package com.example.yanagladdeveloperslife.api

import com.example.yanagladdeveloperslife.models.Gif
import com.example.yanagladdeveloperslife.models.Gifs
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("/random?json=true")
    fun getRandomGif(): Flowable<Gif>

    @GET("/latest/{page}?json=true")
    fun getLatestGifs(
        @Path(value = "page", encoded = true) page: Int,
        @Query(value = "pageSize", encoded = true) pageSize: Int,
        @Query(value = "types", encoded = true) types: String?
    ): Flowable<Gifs>


    @GET("/top/{page}?json=true")
    fun getTopGifs(
        @Path(value = "page", encoded = true) page: Int,
        @Query(value = "pageSize", encoded = true) pageSize: Int,
        @Query(value = "types", encoded = true) types: String?
    ): Flowable<Gifs>
}