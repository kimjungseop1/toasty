package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestMultiCommonListDto {
    @SerializedName("lcode")
    @Expose
    var lcode: Int = -1

    @SerializedName("mcode")
    @Expose
    var mcode: String? = null
}