package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestSubscribeByMeDto {
    @SerializedName("user_id")
    @Expose
    var user_id: String = ""

    @SerializedName("currPage")
    @Expose
    var currPage: Int = -1

    @SerializedName("pageSize")
    @Expose
    var pageSize: Int = -1
}