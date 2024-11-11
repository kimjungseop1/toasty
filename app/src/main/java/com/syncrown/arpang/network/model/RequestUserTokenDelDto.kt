package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestUserTokenDelDto {
    @SerializedName("app_id")
    @Expose
    var app_id: String? = ""

    @SerializedName("device_id")
    @Expose
    var device_id: String? = ""
}