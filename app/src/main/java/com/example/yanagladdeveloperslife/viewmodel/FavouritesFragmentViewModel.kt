package com.example.yanagladdeveloperslife.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.yanagladdeveloperslife.models.GifModel
import com.example.yanagladdeveloperslife.repository.GifRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class FavouritesFragmentViewModel @Inject constructor(private val gifRepository: GifRepository) :
    ViewModel() {

    val favsList: MutableLiveData<List<GifModel>> = MutableLiveData<List<GifModel>>()

    init {
        favsList.value = arrayListOf()
        loadGifsFromDb()
       // Log.d("LIST", " IS ${favsList.value!![0].author}")
    }

    fun loadGifsFromDb() =
        gifRepository.getFavourites().map { list ->
            favsList.value = list
        }

    fun addGifToDb(gifModel: GifModel){
        gifRepository.addGifToFavourites(gifModel)
    }
}