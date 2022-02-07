package com.example.yanagladdeveloperslife.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.yanagladdeveloperslife.db.enitity.GifEntity

@Database(entities = [GifEntity::class], version = 1,
    exportSchema = false)
abstract class FavouritesDatabase : RoomDatabase(){
    abstract fun gifDao() : GifDao
}
