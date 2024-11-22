package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestCartridgeListDto {
    @SerializedName("app_id")
    @Expose
    var app_id: String = ""

    @SerializedName("device_os")
    @Expose
    var device_os: String = ""

    @SerializedName("ctge_no")
    @Expose
    var ctge_no: Int? = null

    @SerializedName("ctge_nm")
    @Expose
    var ctge_nm: String? = null

    @SerializedName("ctge_model")
    @Expose
    var ctge_model: String? = null

    @SerializedName("ctge_model_abbr")
    @Expose
    var ctge_model_abbr: String? = null

    @SerializedName("character_brand")
    @Expose
    var character_brand: String? = null
}