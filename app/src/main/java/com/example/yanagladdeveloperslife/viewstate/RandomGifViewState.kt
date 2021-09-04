package com.example.yanagladdeveloperslife.viewstate

import com.example.yanagladdeveloperslife.models.Gif


sealed class RandomGifViewState {
    object Loading : RandomGifViewState()

    class Loaded(val gif : Gif) : RandomGifViewState()

    class Error{
        object NetworkError : RandomGifViewState()

        object UnexpectedError : RandomGifViewState()
    }

    object SuccessOperation : RandomGifViewState()
}