package com.example.yanagladdeveloperslife.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class ButtonsViewModel : ViewModel() {
    var canLoadPrevious = MutableLiveData(false)
        get() = field
        set(value) {
            field.value = value.value
        }

    var canLoadNext = MutableLiveData(false)
        get() = field
        set(value) {
            field.value = value.value
        }
}
