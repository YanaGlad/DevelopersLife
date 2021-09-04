package com.example.yanagladdeveloperslife.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

class Gifs {
    @SerializedName("result")
    @Expose
    val gifs: ArrayList<Gif>? = null

    @SerializedName("totalCount")
    @Expose
    private val totalCount: Int? = null
}
