package com.example.yanagladdeveloperslife.values


enum class ErrorHandler {
    SUCCESS, LOAD_ERROR, IMAGE_ERROR;

    companion object {
        var currentError = SUCCESS
            get() = field
    }

    fun setSuccess() {
        currentError = SUCCESS
    }

    fun setLoadError() {
        currentError = LOAD_ERROR
    }

    fun setImageError() {
        currentError = IMAGE_ERROR
    }
}
