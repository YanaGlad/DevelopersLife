package com.example.yanagladdeveloperslife.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.yanagladdeveloperslife.ErrorHandler

class GifViewModel : ViewModel() {
    var isCurrentGifLoaded = MutableLiveData(false)
        get() = field
        set(value) {
            field.value = value.value
        }

    var error: MutableLiveData<ErrorHandler> =
        MutableLiveData<ErrorHandler>(ErrorHandler.currentError)
        set(value) {
            field.value = value.value
        }
}
