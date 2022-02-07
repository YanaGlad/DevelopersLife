package com.example.yanagladdeveloperslife.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.yanagladdeveloperslife.ErrorHandler
import com.example.yanagladdeveloperslife.PageOperation
import com.example.yanagladdeveloperslife.models.GifModel
import com.example.yanagladdeveloperslife.models.response.GifResponse
import com.example.yanagladdeveloperslife.repository.GifRepository
import com.example.yanagladdeveloperslife.viewstate.RecyclerGifViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RecyclerFragmentViewModel @Inject constructor(private val gifRepository: GifRepository) : ButtonsViewModel() {

    val type = MutableLiveData<String?>(null)
    val currentPage = MutableLiveData(0)

    var error: MutableLiveData<ErrorHandler> =
        MutableLiveData<ErrorHandler>(ErrorHandler.currentError)

    val favsList: MutableLiveData<List<GifModel>> = MutableLiveData<List<GifModel>>()

    private val _viewState: MutableLiveData<RecyclerGifViewState> = MutableLiveData()

    private val gifModels: MutableLiveData<ArrayList<GifModel>?> = MutableLiveData<ArrayList<GifModel>?>(null)

    init {
        favsList.value = arrayListOf()
        gifModels.value = arrayListOf()
        loadGifsFromDb()
    }

    fun deleteGifFromFavs(gifModel: GifModel) {
        gifRepository.deleteFavourite(gifModel)
    }

    private fun loadGifsFromDb() {
        val disp = gifRepository.getFavourites()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe {
                Log.d("TAG_LIST", " Is... ${it[0].author}")
                favsList.postValue(it)
            }
    }

    fun addGifToDb(gifModel: GifModel) {
        gifRepository.addGifToFavourites(gifModel)
    }

    fun loadRecyclerGifs(type: String) {

        when (type) {
            "latest" -> {
                currentPage.value?.let {
                    gifRepository.getLatestGifs(it, 10, "gifResponse")
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe(
                            { viewState ->
                                run {
                                    _viewState.postValue(viewState)
                                    if (viewState != RecyclerGifViewState.Error.NetworkError)
                                        createListOfGifModels((viewState as RecyclerGifViewState.Loaded).gifsResponse.gifResponses!!)
                                    else {
                                        setError(ErrorHandler.LOAD_ERROR)
                                    }
                                }
                            }, {}
                        )
                }
            }
            "top" -> {
                currentPage.value?.let {
                    gifRepository.getTopGifs(it, 10, "gifResponse")
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe(
                            { viewState ->
                                run {
                                    _viewState.postValue(viewState)
                                    if (viewState != RecyclerGifViewState.Error.NetworkError)
                                        createListOfGifModels((viewState as RecyclerGifViewState.Loaded).gifsResponse.gifResponses!!)
                                    else {
                                        setError(ErrorHandler.LOAD_ERROR)
                                    }
                                }
                            }, {}
                        )
                }
            }
        }
    }
    fun setError(error: ErrorHandler?) {
        this.error.postValue(error)
    }

    fun updateCanLoadPrevious() {
        super.canLoadPrevious =
            MutableLiveData(
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

    private fun createListOfGifModels(gifResponses: ArrayList<GifResponse>) {
        val result: ArrayList<GifModel> = ArrayList<GifModel>()
        for (gif in gifResponses) result.add(gif.createGifModel())
        setGifModels(result)
    }

    fun getGifModels(): MutableLiveData<ArrayList<GifModel>?> {
        return gifModels
    }

    fun getAllFavs(): List<GifModel> {
        var list: List<GifModel> = arrayListOf()
        val disposable = gifRepository.getFavourites()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map {
                list = it
            }
        return list
    }

    private fun setGifModels(gifModels: ArrayList<GifModel>) {
        this.gifModels.postValue(gifModels)
    }
}
