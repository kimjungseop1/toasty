package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestNoticeListDto {
    @SerializedName("lang_code")
    @Expose
    var lang_code: String? = null
}