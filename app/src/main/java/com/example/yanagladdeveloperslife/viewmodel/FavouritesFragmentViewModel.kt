package com.example.yanagladdeveloperslife.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.yanagladdeveloperslife.models.GifModel
import com.example.yanagladdeveloperslife.repository.GifRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


@HiltViewModel
class FavouritesFragmentViewModel @Inject constructor(private val gifRepository: GifRepository) :
    ViewModel() {

    private val favsList: MutableLiveData<List<GifModel>> = MutableLiveData<List<GifModel>>()

    init {
        favsList.value = arrayListOf()
        loadGifsFromDb()
    }

    fun deleteGifFromFavs(gifModel: GifModel){
        gifRepository.deleteFavourite(gifModel)
    }

      fun loadGifsFromDb() {
       val disp =  gifRepository.getFavourites()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe {
                favsList.postValue(it)
            }
    }
}
