package com.example.yanagladdeveloperslife.viewstate

import com.example.yanagladdeveloperslife.models.response.GifResponse


sealed class RandomGifViewState {
    object Loading : RandomGifViewState()

    class Loaded(val gifResponse : GifResponse) : RandomGifViewState()

    class Error{
        object NetworkError : RandomGifViewState()

        object UnexpectedError : RandomGifViewState()
    }

    object SuccessOperation : RandomGifViewState()
}