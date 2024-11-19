package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestCheckNickNameDto {
    @SerializedName("nick_nm")
    @Expose
    var nick_nm: String = ""
}