package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestCommonListDto {
    @SerializedName("lcode")
    @Expose
    var lcode: Int = -1
}