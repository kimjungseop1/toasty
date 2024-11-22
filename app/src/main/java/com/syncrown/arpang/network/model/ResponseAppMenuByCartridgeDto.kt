package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseAppMenuByCartridgeDto {
    @SerializedName("menu_code")
    @Expose
    var menu_code: String? = null

    @SerializedName("menu_nm")
    @Expose
    var menu_nm: String? = null

    @SerializedName("app_id")
    @Expose
    var app_id: String? = null
}