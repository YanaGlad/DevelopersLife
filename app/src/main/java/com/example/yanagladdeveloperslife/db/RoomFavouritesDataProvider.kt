package com.example.yanagladdeveloperslife.db

import android.util.Log
import com.example.yanagladdeveloperslife.db.enitity.GifEntity
import com.example.yanagladdeveloperslife.models.GifModel
import io.reactivex.Flowable
import javax.inject.Inject

class RoomFavouritesDataProvider @Inject constructor(val dao: GifDao) :
    LocalFavouritesDataProvider {
    override fun insertFavourite(item: GifModel) =
        dao.insertFavourite(
            GifEntity(
                id = item.id.toLong(),
                description = item.description,
                votes = item.votes,
                author = item.author,
                date = item.date,
                gifURL = item.gifURL,
                gifSize = item.gifSize,
                previewURL = item.previewURL,
                videoURL = item.videoURL,
                videoPath = item.videoPath,
                videoSize = item.videoSize,
                type = item.type,
                width = item.width,
                height = item.height,
                commentsCount = item.commentsCount,
                fileSize = item.fileSize,
                canVote = item.canVote
            )
        )


    override fun getAllFavourites(): Flowable<List<GifModel>> {
//        dao.getAllFavourites().doOnNext { gifs ->
//            gifs.map {
//                gif ->
//                println(" GIF ${gif.description}")
//            }
//        }.subscribe()

        return dao.getAllFavourites()
            .map { gifs ->
                gifs.map { item ->
                    GifModel(
                        id = item.id.toInt(),
                        description = item.description,
                        votes = item.votes,
                        author = item.author,
                        date = item.date,
                        gifURL = item.gifURL,
                        gifSize = item.gifSize,
                        previewURL = item.previewURL,
                        videoURL = item.videoURL,
                        videoPath = item.videoPath,
                        videoSize = item.videoSize,
                        type = item.type,
                        width = item.width,
                        height = item.height,
                        commentsCount = item.commentsCount,
                        fileSize = item.fileSize,
                        canVote = item.canVote
                    )
                }
            }
    }


    override fun deleteFavourite(item: GifModel) =
        dao.deleteFavourite(
            GifEntity(
                id = item.id.toLong(),
                description = item.description,
                votes = item.votes,
                author = item.author,
                date = item.date,
                gifURL = item.gifURL,
                gifSize = item.gifSize,
                previewURL = item.previewURL,
                videoURL = item.videoURL,
                videoPath = item.videoPath,
                videoSize = item.videoSize,
                type = item.type,
                width = item.width,
                height = item.height,
                commentsCount = item.commentsCount,
                fileSize = item.fileSize,
                canVote = item.canVote
            )
        )
}