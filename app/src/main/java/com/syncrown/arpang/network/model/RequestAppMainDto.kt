package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestAppMainDto {
    @SerializedName("app_id")
    @Expose
    var app_id: String? = null
}