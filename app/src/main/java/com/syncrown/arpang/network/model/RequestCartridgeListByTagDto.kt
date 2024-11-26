package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestCartridgeListByTagDto {
    @SerializedName("tag_seq_no")
    @Expose
    var tag_seq_no: Int = -1

    @SerializedName("app_id")
    @Expose
    var app_id: String = ""
}