package com.example.yanagladdeveloperslife.models.response

import com.example.yanagladdeveloperslife.models.GifModel
import com.google.gson.annotations.SerializedName

data class GifResponse(
    @field:SerializedName("id") val id: Int?,
    @field:SerializedName("description") val description: String?,
    @field:SerializedName("votes") val votes: Int?,
    @field:SerializedName("author") val author: String?,
    @field:SerializedName("date") val date: String?,
    @field:SerializedName("gifURL") val gifURL: String?,
    @field:SerializedName("gifSize") val gifSize: Int?,
    @field:SerializedName("previewURL") val previewURL: String?,
    @field:SerializedName("videoURL") val videoURL: String?,
    @field:SerializedName("videoPath") val videoPath: String?,
    @field:SerializedName("videoSize") val videoSize: Int?,
    @field:SerializedName("type") val type: String?,
    @field:SerializedName("width") val width: String?,
    @field:SerializedName("height") val height: String?,
    @field:SerializedName("commentsCount") val commentsCount: Int?,
    @field:SerializedName("fileSize") val fileSize: Int?,
    @field:SerializedName("canVote") val canVote: Boolean?,
) {
    fun createGifModel(): GifModel {
        return GifModel(
            id!!,
            description!!,
            votes!!,
            author!!,
            date!!,
            gifURL,
            gifSize!!,
            previewURL!!,
            videoURL,
            videoPath ,
            videoSize,
            type!!,
            width!!,
            height!!,
            commentsCount!!,
            fileSize!!,
            canVote!!
        )
    }
}
