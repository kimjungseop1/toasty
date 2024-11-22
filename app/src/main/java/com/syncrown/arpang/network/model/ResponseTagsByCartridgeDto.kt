package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseTagsByCartridgeDto {
    @SerializedName("tag_seq_no")
    @Expose
    var tag_seq_no: Int? = null

    @SerializedName("tag_nm")
    @Expose
    var tag_nm: String? = null

    @SerializedName("app_id")
    @Expose
    var app_id: String? = null
}