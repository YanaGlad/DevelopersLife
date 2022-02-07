package com.example.yanagladdeveloperslife.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.yanagladdeveloperslife.ErrorHandler
import com.example.yanagladdeveloperslife.models.GifModel
import com.example.yanagladdeveloperslife.repository.GifRepository
import com.example.yanagladdeveloperslife.viewstate.RandomGifViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RandomFragmentViewModel @Inject constructor(private val gifRepository: GifRepository) : ButtonsViewModel() {

    private val isCurrentGifLoaded = MutableLiveData(false)
    private val currentGif: MutableLiveData<GifModel?> = MutableLiveData(null)
    private val error: MutableLiveData<ErrorHandler> = MutableLiveData<ErrorHandler>(ErrorHandler.currentError)
    private val gifModels: MutableList<GifModel> = ArrayList<GifModel>()

    private val _viewState: MutableLiveData<RandomGifViewState> = MutableLiveData()
    val viewState: LiveData<RandomGifViewState>
        get() = _viewState

    fun dispose(){
        gifRepository.dispose()
    }

    fun getError(): MutableLiveData<ErrorHandler> {
        return error
    }

    fun setAppError(error: ErrorHandler) {
        this.error.value = error
    }

    fun getIsCurrentGifLoaded(): LiveData<Boolean> {
        return isCurrentGifLoaded
    }

    fun addGifToDb(gifModel: GifModel) {
        gifRepository.addGifToFavourites(gifModel)
    }

    fun getGifById(gifModel: GifModel): GifModel {
        var result: GifModel? = null
        val disposable = gifRepository.getFavById(gifModel)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())

            .subscribe(
                { viewState ->
                    result = viewState
                },
                {

                }
            )
        return result!!
    }

    fun setIsCurrentGifLoaded(isCurrentGifLoaded: Boolean) {
        this.isCurrentGifLoaded.value = isCurrentGifLoaded
    }

    fun updateCanLoadPrevious() {
        val hasErrors: Boolean = !(error.value == ErrorHandler.SUCCESS)
        Log.d("CAN_LOAD_PREV", "Pos  ${(pos - 1)}  noerrors ${!hasErrors} and final.. ${!hasErrors && pos - 1 >= 0}")
        super.canLoadPrevious = MutableLiveData(!hasErrors && pos - 1 >= 0)
    }

    fun getCurrentGif(): LiveData<GifModel?> {
        return currentGif
    }

    fun loadRandomGif() =
        gifRepository.getRandomGif()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                { viewState -> _viewState.postValue(viewState) },
                {}
            )

    fun addGifModel(gifModel: GifModel) {
        Log.d("Add", "POS $pos")
        currentGif.value = gifModel
        gifModels.add(gifModel)
        pos++
        cachedCurrentGif = gifModels[gifModels.size - 1]
        updateCanLoadPrevious()
    }

    fun goNext(): Boolean {
        Log.d("Next", "POS $pos")
        if (cachedCurrentGif != null && pos < gifModels.size - 1) {
            pos++
            cachedCurrentGif = gifModels[pos]
            currentGif.setValue(cachedCurrentGif)
            updateCanLoadPrevious()
            return true
        }
        updateCanLoadPrevious()
        return false
    }

    fun goBack(): Boolean {
        Log.d("Prev", "POS $pos")
        if (pos - 1 >= 0) {
            pos--
            cachedCurrentGif = gifModels[pos]
            currentGif.setValue(cachedCurrentGif)
            updateCanLoadPrevious()
            return true
        }
        updateCanLoadPrevious()
        return false
    }

    companion object {
        private var cachedCurrentGif: GifModel? = null
        private var pos = -1
    }
}
