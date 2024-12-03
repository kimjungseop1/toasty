package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestMyFavoriteDto {
    @SerializedName("search_nm")
    @Expose
    var search_nm: String = ""
}