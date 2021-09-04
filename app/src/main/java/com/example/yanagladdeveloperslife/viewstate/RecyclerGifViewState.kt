package com.example.yanagladdeveloperslife.viewstate

import com.example.yanagladdeveloperslife.models.Gifs

sealed class RecyclerGifViewState {
    object Loading : RecyclerGifViewState()

    class Loaded(val gifs : Gifs) : RecyclerGifViewState()

    class Error{
        object NetworkError : RecyclerGifViewState()

        object UnexpectedError : RecyclerGifViewState()
    }

    object SuccessOperation : RecyclerGifViewState()
}