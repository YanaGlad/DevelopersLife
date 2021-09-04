package com.example.yanagladdeveloperslife.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.yanagladdeveloperslife.models.Gif
import com.example.yanagladdeveloperslife.models.GifModel
import com.example.yanagladdeveloperslife.repository.GifRepository
import com.example.yanagladdeveloperslife.values.ErrorHandler
import com.example.yanagladdeveloperslife.values.PageOperation
import com.example.yanagladdeveloperslife.viewstate.RecyclerGifViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RecyclerFragmentViewModel @Inject constructor(private val gifRepository: GifRepository) :
    PageViewModel() {
    val type = MutableLiveData<String?>(null)
    val currentPage = MutableLiveData(0)
        get() = field

    var error: MutableLiveData<ErrorHandler> = MutableLiveData<ErrorHandler>(ErrorHandler.currentError)

    private val _viewState: MutableLiveData<RecyclerGifViewState> = MutableLiveData()
    val viewState: LiveData<RecyclerGifViewState>
        get() = _viewState


    fun loadRecyclerGifs(type: String) {
        var response: Disposable? = null

        when (type) {
            "latest" -> {
                response = currentPage.value?.let {
                    gifRepository.getLatestGifs(it, 10, "gif")
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe(
                            { viewState ->
                                run {
                                    _viewState.postValue(viewState)
                                    if (viewState != RecyclerGifViewState.Error.NetworkError)
                                        createListOfGifModels((viewState as RecyclerGifViewState.Loaded).gifs.gifs!!)
                                    else {
                                        setError(ErrorHandler.LOAD_ERROR)
                                    }
                                }
                            },
                            {

                            }
                        )

                }

            }
            "top" -> {
                response = currentPage.value?.let {
                    gifRepository.getTopGifs(it, 10, "gif")
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe(
                            { viewState ->
                                run {
                                    _viewState.postValue(viewState)
                                    if (viewState != RecyclerGifViewState.Error.NetworkError)
                                        createListOfGifModels((viewState as RecyclerGifViewState.Loaded).gifs.gifs!!)
                                    else {
                                        setError(ErrorHandler.LOAD_ERROR)
                                    }
                                }
                            },
                            {

                            }
                        )

                }
            }
        }
    }

    fun setError(error: ErrorHandler?) {
        this.error.postValue(error)
    }

    private val gifModels: MutableLiveData<ArrayList<GifModel>?> =
        MutableLiveData<ArrayList<GifModel>?>(null)

    fun updateCanLoadPrevious() {
        super.setCanLoadPrevious(
            currentPage.value!! > 0 &&
                    error.getValue() == ErrorHandler.SUCCESS
        )
    }

    fun setType(type: String?) {
        this.type.value = type
    }

    fun setCurrentPage(currentPage: Int, pageOperation: PageOperation) {
        this.currentPage.value = currentPage + pageOperation.pos
    }

    private fun createListOfGifModels(gifs: ArrayList<Gif>) {
        val result: ArrayList<GifModel> = ArrayList<GifModel>()
        for (gif in gifs) result.add(gif.createGifModel())
        setGifModels(result)
    }

    fun getGifModels(): MutableLiveData<ArrayList<GifModel>?> {
        return gifModels
    }

    private fun setGifModels(gifModels: ArrayList<GifModel>) {
        this.gifModels.postValue(gifModels)
    }
}