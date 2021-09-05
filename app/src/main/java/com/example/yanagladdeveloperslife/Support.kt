package com.example.yanagladdeveloperslife

enum class ErrorHandler {
    SUCCESS, LOAD_ERROR, IMAGE_ERROR;

    companion object {
        var currentError = SUCCESS
            get() = field
    }

}
enum class PageOperation(val pos: Int) {
    STAND(0), NEXT(1), PREVIOUS(-1);
}
