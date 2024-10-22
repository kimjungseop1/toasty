package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseNoticeListDto {
    @SerializedName("bbsid")
    @Expose
    var bbsid: Int? = null

    @SerializedName("read_se")
    @Expose
    var read_se: String? = null
}