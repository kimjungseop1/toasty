package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestAppMenuByCartridgeDto {
    @SerializedName("ctge_no")
    @Expose
    var ctge_no: Int = -1

    @SerializedName("app_id")
    @Expose
    var app_id: String = ""
}