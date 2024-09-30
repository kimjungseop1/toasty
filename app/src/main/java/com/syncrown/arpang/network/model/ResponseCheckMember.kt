package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseCheckMember {
    @SerializedName("msgCode")
    @Expose
    val msgCode: String? = null

    @SerializedName("member_check")
    @Expose
    val member_check: Int = -1
}