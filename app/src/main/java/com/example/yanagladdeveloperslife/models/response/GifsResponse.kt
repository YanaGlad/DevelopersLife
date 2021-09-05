package com.example.yanagladdeveloperslife.models.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

class GifsResponse {
    @SerializedName("result")
    @Expose
    val gifResponses: ArrayList<GifResponse>? = null

    @SerializedName("totalCount")
    @Expose
    private val totalCount: Int? = null
}
