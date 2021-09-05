package com.example.yanagladdeveloperslife.repository

import com.example.yanagladdeveloperslife.network.RemoteDataProvider
import com.example.yanagladdeveloperslife.db.LocalFavouritesDataProvider
import com.example.yanagladdeveloperslife.models.GifModel
import com.example.yanagladdeveloperslife.viewstate.RandomGifViewState
import com.example.yanagladdeveloperslife.viewstate.RecyclerGifViewState
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import javax.inject.Inject

class GifRepositoryImpl @Inject constructor(
    private val remoteDataProvider: RemoteDataProvider,
    private val localDataProvider: LocalFavouritesDataProvider
) : GifRepository {

    override fun getRandomGif(): Single<RandomGifViewState> =
        Single.create { emitter ->
            remoteDataProvider.getRandomGif()
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { gifs ->
                        emitter.onSuccess(RandomGifViewState.Loaded(gifs))
                    },
                    {
                        when (it) {
                            is IOException -> emitter.onSuccess(RandomGifViewState.Error.NetworkError)
                            else -> emitter.onSuccess(RandomGifViewState.Error.UnexpectedError)
                        }
                    }
                )
        }


    private fun Throwable.convertToViewState(): RandomGifViewState =
        when (this) {
            is IOException -> RandomGifViewState.Error.NetworkError
            else -> RandomGifViewState.Error.UnexpectedError
        }

    override fun getLatestGifs(
        page: Int,
        pageSize: Int,
        types: String?
    ): Single<RecyclerGifViewState> =
        Single.create { emitter ->
            remoteDataProvider.getLatestGifs(page, pageSize, types)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { gifs ->
                        emitter.onSuccess(RecyclerGifViewState.Loaded(gifs))
                    },
                    {
                        when (it) {
                            is IOException -> emitter.onSuccess(RecyclerGifViewState.Error.NetworkError)
                            else -> emitter.onSuccess(RecyclerGifViewState.Error.UnexpectedError)
                        }
                    }
                )
        }

    override fun getTopGifs(
        page: Int,
        pageSize: Int,
        types: String?
    ): Single<RecyclerGifViewState> =
        Single.create { emitter ->
            remoteDataProvider.getTopGifs(page, pageSize, types)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { gifs ->
                        emitter.onSuccess(RecyclerGifViewState.Loaded(gifs))
                    },
                    {
                        when (it) {
                            is IOException -> emitter.onSuccess(RecyclerGifViewState.Error.NetworkError)
                            else -> emitter.onSuccess(RecyclerGifViewState.Error.UnexpectedError)
                        }
                    }
                )
        }

    override fun addGifToFavourites(gifModel: GifModel) {
        localDataProvider.insertFavourite(gifModel)
    }

    override fun getFavourites(): Flowable<List<GifModel>> =
        localDataProvider.getAllFavourites()

    override fun getFavById(gifModel: GifModel): Single<GifModel> =
        localDataProvider.getGifByDescription(gifModel.id.toLong())

    override fun deleteFavourite(item: GifModel) {
        localDataProvider.deleteFavourite(item)
    }


}