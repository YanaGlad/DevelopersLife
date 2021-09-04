package com.example.yanagladdeveloperslife.db.enitity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourites")
data class GifEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val description: String,
    val votes: Int,
    val author: String,
    val date: String,
    val gifURL: String?,
    val gifSize: Int,
    val previewURL: String?,
    val videoURL: String?,
    val videoPath: String?,
    val videoSize: Int?,
    val type: String,
    val width: String,
    val height: String,
    val commentsCount: Int,
    val fileSize: Int,
    val canVote: Boolean
)