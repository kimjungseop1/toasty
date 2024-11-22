package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestCartridgeByAppMenuDto {
    @SerializedName("menu_code")
    @Expose
    var menu_code: String = ""

    @SerializedName("app_id")
    @Expose
    var app_id: String = ""
}