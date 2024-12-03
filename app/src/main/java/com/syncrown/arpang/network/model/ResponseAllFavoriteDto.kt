package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseAllFavoriteDto {
    @SerializedName("root")
    @Expose
    var root : ArrayList<Root> = ArrayList()

    class Root {
        @SerializedName("hashtag_nm")
        @Expose
        var hashtag_nm: String? = null

        @SerializedName("count")
        @Expose
        var count: Int? = null
    }
}