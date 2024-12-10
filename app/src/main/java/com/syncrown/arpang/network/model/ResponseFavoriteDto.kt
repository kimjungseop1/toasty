package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseFavoriteDto {
    @SerializedName("msgCode")
    @Expose
    var msgCode: String? = null

    @SerializedName("favorite_cnt")
    @Expose
    var favorite_cnt: Int? = null
}