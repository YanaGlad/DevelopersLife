package com.example.yanagladdeveloperslife.db

import com.example.yanagladdeveloperslife.models.GifModel
import io.reactivex.Flowable
import javax.inject.Inject

class RoomFavouritesDataProvider  @Inject constructor(val dao: GifDao) : LocalFavouritesDataProvider {
    override fun insertFavourite(item: GifModel) {
        dao
    }

    override fun getAllCategoriesByIncome(id: Long): Flowable<List<GifModel>> {
        TODO("Not yet implemented")
    }

    override fun getAllFavourites(): Flowable<List<GifModel>> {
        TODO("Not yet implemented")
    }

    override fun deleteFavourite(item: GifModel) {
        TODO("Not yet implemented")
    }
}