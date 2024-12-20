package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestCheckNickNameDto {
    @SerializedName("user_id")
    @Expose
    var user_id: String = ""

    @SerializedName("app_id")
    @Expose
    var app_id: String = ""

    @SerializedName("nick_nm")
    @Expose
    var nick_nm: String = ""
}