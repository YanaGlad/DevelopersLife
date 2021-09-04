package com.example.yanagladdeveloperslife.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class PageViewModel : ViewModel() {
    private val canLoadPrevious = MutableLiveData(false)
    private val canLoadNext = MutableLiveData(false)

    open fun getCanLoadPrevious(): LiveData<Boolean> {
        return canLoadPrevious
    }

    fun setCanLoadPrevious(canLoadPrevious: Boolean) {
        this.canLoadPrevious.value = canLoadPrevious
    }

    open fun getCanLoadNext(): LiveData<Boolean> {
        return canLoadNext
    }

    open fun setCanLoadNext(canLoadNext: Boolean) {
        this.canLoadNext.value = canLoadNext
    }
}

