package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseCartridgeListByTagDto {
    @SerializedName("ctge_no")
    @Expose
    var ctge_no: Int? = null

    @SerializedName("ctge_nm")
    @Expose
    var ctge_nm: String? = null

    @SerializedName("app_id")
    @Expose
    var app_id: String? = null
}