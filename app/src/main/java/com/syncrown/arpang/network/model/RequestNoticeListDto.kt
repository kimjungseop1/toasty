package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestNoticeListDto {
    @SerializedName("app_id")
    @Expose
    var app_id: String = ""

    @SerializedName("lang_code")
    @Expose
    var lang_code: String? = null
}