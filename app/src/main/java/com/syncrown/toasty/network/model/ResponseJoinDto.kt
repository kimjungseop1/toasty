package com.syncrown.toasty.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseJoinDto {
    @SerializedName("msgCode")
    @Expose
    val msgCode: String = ""
}