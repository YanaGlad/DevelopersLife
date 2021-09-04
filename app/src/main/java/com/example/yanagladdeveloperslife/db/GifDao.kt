package com.example.yanagladdeveloperslife.db

import androidx.room.*
import com.example.yanagladdeveloperslife.db.enitity.GifEntity
import io.reactivex.Flowable

@Dao
interface GifDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavourite(item : GifEntity)

    @Query(value = "Select * from `favourites` where id = :id " )
    fun getAllCategoriesByIncome(id : Long) : Flowable<List<GifEntity>>

    @Query("Select * from `favourites`" )
    fun getAllFavourites() : Flowable<List<GifEntity>>

    @Delete()
    fun deleteFavourite(item : GifEntity)

}