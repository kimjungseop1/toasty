package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestUserTokenRegDto {
    @SerializedName("user_token")
    @Expose
    var user_token: String? = ""

    @SerializedName("app_id")
    @Expose
    var app_id: String? = ""

    @SerializedName("device_id")
    @Expose
    var device_id: String? = ""

    @SerializedName("user_id")
    @Expose
    var user_id: String? = null
}