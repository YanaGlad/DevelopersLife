package com.example.yanagladdeveloperslife.viewstate

import com.example.yanagladdeveloperslife.models.response.GifsResponse

sealed class RecyclerGifViewState {
    object Loading : RecyclerGifViewState()

    class Loaded(val gifsResponse : GifsResponse) : RecyclerGifViewState()

    class Error{
        object NetworkError : RecyclerGifViewState()

        object UnexpectedError : RecyclerGifViewState()
    }
    object SuccessOperation : RecyclerGifViewState()
}
