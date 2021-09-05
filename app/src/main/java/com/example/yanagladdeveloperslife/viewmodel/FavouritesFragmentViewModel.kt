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

    val favsList: MutableLiveData<List<GifModel>> = MutableLiveData<List<GifModel>>()

    init {
        favsList.value = arrayListOf()
        loadGifsFromDb()
    }

    private fun loadGifsFromDb() {
       val disp =  gifRepository.getFavourites()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe {
                Log.d("TAG_LIST", " Is... ${it[0].author}")
                favsList.postValue(it)
            }
    }

    fun addGifToDb(gifModel: GifModel){
        gifRepository.addGifToFavourites(gifModel)
    }
}