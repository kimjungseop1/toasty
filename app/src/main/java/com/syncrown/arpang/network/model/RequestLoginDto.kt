package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestLoginDto {
    @SerializedName("user_id")
    @Expose
    val user_id: String = ""
}