package com.example.yanagladdeveloperslife.db

import androidx.room.*
import com.example.yanagladdeveloperslife.db.enitity.GifEntity
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface GifDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavourite(item : GifEntity)

    @Query("Select * from `favourites`" )
    fun getAllFavourites() : Flowable<List<GifEntity>>

    @Query(value = "Select * from `favourites` where id = :id " )
    fun getGifByDescription(id : Long) : Single<GifEntity>

    @Delete()
    fun deleteFavourite(item : GifEntity)
}
